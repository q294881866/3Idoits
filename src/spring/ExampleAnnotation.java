package spring;
/**
 * 注解简单实用
 * public enum RetentionPolicy {    SOURCE,    CLASS,     RUNTIME    }  
 * SOURCE表示这个Annotation类型的信息只会保留在源码里，源码经过编译之后，Annotation的数据就会消失，并不会保留在编译好的.class文件里；
 *    CLASS表示这个Annotation类型的信息在源码保留，在.class文件也保留，但不会把这些信息加载到虚拟机（JVM）中，如果不设置，系统默认值是CLASS；                          
 *    RUNTIME表示在源码，编译后的.class都保存信息，在执行的时候也会把这些信息加载到JVM中                                                           
 *    ElementType.java                                                                                            
 *    public enum ElementType {                                                                                   
 *    TYPE, FIELD, METHOD, PARAMETER,                                                                             
 *    CONSTRUCTOR,  LOCAL_VARIABLE,                                                                               
 *    ANNOTATION_TYPE,PACKAGE }                                                                                   
 *    @Target里面的ElementType是用来制定Annotation可以用在那一种类型，说明一下                                                          
 *    TYPE(类型),FILED(字段)METHOD(方法)PARAMETER(参数)CONSTRUCTOR（构造器）LOCAL_VARIABLE(局部变量)                               
 *    ANNITATION_TYPE（注解）PACKAGE（包）其中TYPE是指可以用在class，interface，eumn，annotation上面，如果没有制定，那么它可以用在任何上面               
 *    如果想把annotation的数据继承给子类，那就需要用到@Inherited这个annotation类型                                                       
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface ExampleAnnotation {
	
}


