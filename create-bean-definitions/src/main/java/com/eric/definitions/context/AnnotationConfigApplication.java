package com.eric.definitions.context;

import com.eric.definitions.annotation.*;
import com.eric.definitions.exception.BeanDefinitionException;
import com.eric.definitions.exception.BeanNotOfRequiredTypeException;
import com.eric.definitions.exception.NoUniqueBeanDefinitionException;
import com.eric.definitions.io.PropertyResolver;
import com.eric.definitions.io.ResourceResolver;
import com.eric.definitions.utils.ClassUtils;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationConfigApplication {

    protected  final Logger logger = LoggerFactory.getLogger(getClass());

    protected  final PropertyResolver propertyResolver;

    protected final Map<String, BeanDefinition> beans;

    public AnnotationConfigApplication(Class<?> configClass, PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
        // 扫描获取所有Bean的Class类型：
        final Set<String> beanClassNames = scanForClassNames(configClass);
        // 创建Bean的定义：
        this.beans = createBeanDefinitions(beanClassNames);

    }

    Map<String, BeanDefinition> createBeanDefinitions(Set<String> classNameSet) {
        Map<String, BeanDefinition> defs = new HashMap<>();
        for (String className : classNameSet) {
            // 获取Class:
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new BeanDefinitionException(String.format("Class %s not found.", className), e);
            }
            if (clazz.isAnnotation() || clazz.isInterface() || clazz.isEnum() || clazz.isRecord()) {
                continue;
            }
            // 是否标注@Component ?
            Component component = ClassUtils.findAnnotation(clazz, Component.class);
            if (component != null) {
                logger.atDebug().log("found component: {}", clazz.getName());
                int mod = clazz.getModifiers();
                if (Modifier.isAbstract( mod)) {
                    throw new BeanDefinitionException(String.format("@Component class %s must not be abstract.", clazz.getName()));
                }
                if (Modifier.isPrivate( mod)) {
                    throw new BeanDefinitionException(String.format("@Component class %s must not be private.", clazz.getName()));
                }
                String beanName = ClassUtils.getBeanName(clazz);
                var def = new BeanDefinition(beanName, clazz, getSuitableConstructor(clazz), getOrder(clazz),
                        clazz.isAnnotationPresent(Primary.class),
                        // named init / destroy method:
                        null, null,
                        // init method
                        ClassUtils.findAnnotationMethod(clazz, PostConstruct.class),
                        // destroy method
                        ClassUtils.findAnnotationMethod(clazz, PreDestroy.class));
                addBeanDefinitions(defs, def);
                logger.atDebug().log("define bean: {}", def);
                Configuration configuration = ClassUtils.findAnnotation(clazz, Configuration.class);
                if (configuration != null) {
                    scanFactoryMethods(beanName, clazz, defs);
                }
            }
        }
        return defs;
    }

    /**
     * Get public constructor or non-public constructor as fallback
     * @param clazz
     * @return
     */
    Constructor<?> getSuitableConstructor(Class<?> clazz) {
        Constructor<?>[] cons = clazz.getConstructors();
        if (cons.length == 0) {
            cons = clazz.getDeclaredConstructors();
            if (cons.length != 1) {
                throw new BeanDefinitionException(String.format("More than one constructor found in class %s.", clazz.getName()));
            }
        }
        if (cons.length != 1) {
            throw new BeanDefinitionException(String.format("More than one constructor found in class %s.", clazz.getName()));
        }
        return cons[0];
    }

    /**
     * scan factory method that annotated with @Bean:
     * <code>
     * @Configuration
     * public class Hello {
     * @Bean
     * ZoneId createZone() {
     *     return ZoneId.of("Asia/Shanghai");
     * }
     *     }
     * </code>
     * @param factoryBeanName
     * @param clazz
     * @param defs
     */
    void scanFactoryMethods(String factoryBeanName, Class<?> clazz, Map<String, BeanDefinition> defs) {
        for (Method method : clazz.getDeclaredMethods()) {
            Bean bean = method.getAnnotation(Bean.class);
            if (bean != null) {
                int mod = method.getModifiers();
                if (Modifier.isAbstract( mod)) {
                    throw new BeanDefinitionException(String.format("@Bean method %s.%s must not be abstract.", clazz.getName(), method.getName()));
                }
                if (Modifier.isFinal(mod)) {
                    throw new BeanDefinitionException(String.format("@Bean method %s.%s must not be final.", clazz.getName(), method.getName()));
                }
                if (Modifier.isPrivate(mod)) {
                    throw new BeanDefinitionException(String.format("@Bean method %s.%s must not be private.", clazz.getName(), method.getName()));
                }
                Class<?> beanClass = method.getReturnType();
                if (beanClass.isPrimitive()) {
                    throw new BeanDefinitionException(String.format("@Bean method %s.%s must not return a primitive type: %s", clazz.getName(), method.getName(), beanClass.getName()));
                }
                if (beanClass == void.class || beanClass == Void.class) {
                    throw new BeanDefinitionException(String.format("@Bean method %s.%s must not return void.", clazz.getName(), method.getName()));
                }
                var def = new BeanDefinition(ClassUtils.getBeanName(method), beanClass, factoryBeanName,
                        method, getOrder(method),
                        method.isAnnotationPresent(Primary.class),
                        // init method:
                        bean.initMethod().isEmpty() ? null : bean.initMethod(),
                        // destroy method:
                        bean.destroyMethod().isEmpty() ? null : bean.destroyMethod(),
                        // @PostConstruct / @PreDestroy method
                        null, null
                        );
                addBeanDefinitions(defs, def);
                logger.atDebug().log("bean definition found: {}", def);
            }
        }
    }

    /**
     * check and add bean definitions
     */
    void addBeanDefinitions(Map<String, BeanDefinition> defs, BeanDefinition def) {
        if (defs.put(def.getName(), def) != null) {
            throw new BeanDefinitionException("Duplicate bean name: " + def.getName());
        }
    }

    /**
     * Get order by:
     * <code>
     * @Component
     * @Order(100)
     *     public class Hello {}
     * </code>
     * @param clazz
     * @return
     */
    int getOrder(Class<?> clazz) {
        Order order = clazz.getAnnotation(Order.class);
        return order == null ? Integer.MAX_VALUE : order.value();
    }

    /**
     * Get order by:
     * <code>
     * @Bean
     * @Order(100)
     *     Hello createHello() {
     *         return new Hello();
     *     }
     * </code>
     * @param method
     * @return
     */
    int getOrder(Method method) {
        Order order = method.getAnnotation(Order.class);
        return order == null ? Integer.MAX_VALUE : order.value();
    }

    /**
     * Do Component scan and return class names
     * @param configClass
     * @return
     */
    protected Set<String> scanForClassNames(Class<?> configClass) {
        // 获取要扫描的package名称:
        ComponentScan scan = ClassUtils.findAnnotation(configClass, ComponentScan.class);
        final String[] scanPackages = scan == null || scan.value().length == 0 ? new String[] { configClass.getPackage().getName() } : scan.value();;
        logger.atInfo().log("component scan in packages: {}", Arrays.toString(scanPackages));
        Set<String> classNameSet = new HashSet<>();
        for (String pkg : scanPackages) {
            // 扫描package:
            logger.atDebug().log("scan package: {}", pkg);
            var rr = new ResourceResolver(pkg);
            List<String> classList = rr.scan(res -> {
                String name = res.name();
                if (name.endsWith(".class")) {
                    return name.substring(0, name.length() - 6).replace('/', '.').replace('\\', '.');
                }
                return null;
            });
            if (logger.isDebugEnabled()) {
                classList.forEach(className -> {
                    logger.debug("class found by component scan: {}", className);
                });
            }
            classNameSet.addAll(classList);
        }
        // 查找 @Import(Xyz.class):
        Import importConfig = configClass.getAnnotation(Import.class);
        if (importConfig != null) {
            for (Class<?> importConfigClass : importConfig.value()) {
                String importClassName = importConfigClass.getName();
                if (classNameSet.contains(importClassName)) {
                    logger.warn("ignore import: {} for it is already been scanned", importClassName);
                } else {
                    logger.debug("class found by import: {}", importClassName);
                    classNameSet.add(importClassName);
                }
            }
        }
        return classNameSet;

    }

    boolean isConfigurationDefinition(BeanDefinition def) {
        return ClassUtils.findAnnotation(def.getBeanClass(), Configuration.class) != null;

    }

    /**
     * 根据Name查找BeanDefinition， 如果Name不存在，返回Null
     * @return
     */
    @Nullable
    public BeanDefinition findBeanDefinition(String name) {
        return this.beans.get(name);
    }

    @Nullable
    public BeanDefinition findBeanDefinition(String name, Class<?> requiredType) {
        BeanDefinition def = findBeanDefinition(name);
        if (def == null) {
            return null;
        }
        if (!requiredType.isAssignableFrom(def.getBeanClass())) {
            throw new BeanNotOfRequiredTypeException(String.format(
                    "Autowire required type '%s' but bean '%s' has actual type: '%s'.", requiredType.getName(),
                    name, def.getBeanClass().getName()));
        }
        return def;
    }

    /**
     * 根据Type查找BeanDefinition, 返回0个或多个
     * @param type
     * @return
     */
    public List<BeanDefinition> findBeanDefinitions(Class<?> type) {
        return this.beans.values().stream()
                .filter(def -> type.isAssignableFrom(def.getBeanClass()))
                .sorted().collect(Collectors.toList());
    }

    /**
     * 根据Type查找某个BeanDefinition，如果不存在返回null， 如果存在多个
     * 返回@Primaty标注的一个，如果有多个@Primary标注，或没有@Primary标注但找到多个，均抛出
     * NoUniqueBeanDefinitionException
     * @param type
     * @return
     */
    public BeanDefinition findBeanDefinition(Class<?> type) {
        List<BeanDefinition> defs = findBeanDefinitions(type);
        if (defs.isEmpty()) {
            return null;
        }
        if (defs.size() == 1) {
            return defs.get(0);
        }
        // more than 1 beans, require @Primary
        List<BeanDefinition> primaryDefs = defs.stream().filter(def -> def.isPrimary()).collect(Collectors.toList());
        if (primaryDefs.size() == 1) {
            return primaryDefs.get(0);
        }

        if (primaryDefs.isEmpty()) {
            throw new NoUniqueBeanDefinitionException(String.format("Multiple bean with type '%s' found, but no @Primary specified.", type.getName()));
        } else {
            throw new NoUniqueBeanDefinitionException(String.format("Multiple bean with type '%s' found, but more than one @Primary specified.", type.getName()));

        }


    }





}
