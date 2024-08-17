package io.determann.shadow.consistency.property;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.PropertyLangModel;
import io.determann.shadow.api.shadow.Nameable;
import io.determann.shadow.api.shadow.QualifiedNameable;
import io.determann.shadow.implementation.support.internal.property.PropertyTemplate;
import io.determann.shadow.implementation.support.internal.property.PropertyTemplateFactory;
import org.junit.jupiter.api.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PropertyTemplateFactoryTest
{
   @Test
   void templatesFor()
   {
      ProcessorTest.process(api ->
                            {
                               BeanInfo beanInfo = getBeanInfo(Pojo.class);
                               List<PropertyTemplate> templates =
                                     PropertyTemplateFactory.templatesFor(api.getClassOrThrow(Pojo.class.getName()));

                               assertEquals(beanInfo.getPropertyDescriptors().length, templates.size());

                               Map<String, PropertyDescriptor> nameDescriptor = Arrays.stream(beanInfo.getPropertyDescriptors())
                                                                                      .collect(Collectors.toMap(PropertyDescriptor::getName,
                                                                                                                Function.identity()));

                               for (PropertyTemplate template : templates)
                               {
                                  PropertyDescriptor descriptor = nameDescriptor.get(template.getName());

                                  assertNotNull(descriptor);

                                  assertEquals(descriptor.getName(), template.getName());
                                  assertEquals(descriptor.getPropertyType().toString().replaceAll("[a-z]+ ", ""), getName(template));
                                  assertEquals(descriptor.getReadMethod().getName(), template.getGetter().toString().replace("()", ""));
                                  if (descriptor.getWriteMethod() == null || template.getSetter() == null)
                                  {
                                     assertEquals(descriptor.getWriteMethod(), template.getSetter());
                                  }
                                  else
                                  {
                                     assertEquals(descriptor.getWriteMethod().getName(), query((Nameable) template.getSetter()).getName());
                                  }
                               }
                            })
                   .withCompiledClass(Pojo.class)
                   .compile();
   }

   private static String getName(PropertyTemplate template)
   {
      if (template.getType() instanceof QualifiedNameable qualifiedNameable)
      {
         return requestOrThrow(qualifiedNameable, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
      }
      if (template.getType() instanceof Nameable nameable)
      {
         return requestOrThrow(nameable, NAMEABLE_GET_NAME);
      }
      return template.getType().toString();
   }

   private static BeanInfo getBeanInfo(Class<Pojo> clazz)
   {
      try
      {
         return Introspector.getBeanInfo(clazz);
      }
      catch (IntrospectionException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Test
   void overwrittenAccessor()
   {
      ProcessorTest.process(context ->
                            {
                               List<PropertyLangModel> properties = context.getClassOrThrow("Child").getProperties();

                               assertEquals(2, properties.size());
                               assertEquals("class", properties.get(0).getName());
                               assertEquals("id", properties.get(1).getName());
                               assertEquals("Child", properties.get(1).getGetter().getSurrounding().getName());
                            })
                   .withCodeToCompile("Parent", "abstract class Parent{public abstract Long getId();}")
                   .withCodeToCompile("Child", "abstract class Child extends Parent{public abstract Long getId();}")
                   .compile();
   }
}