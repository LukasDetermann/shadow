package org.determann.shadow.impl;

import org.determann.shadow.api.AnnotationTypeChooser;
import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.*;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableSet;

public class AnnotationTypeChooserImpl implements AnnotationTypeChooser
{
   private final Set<Shadow<TypeMirror>> shadows;
   private final ShadowApi shadowApi;

   AnnotationTypeChooserImpl(ShadowApi shadowApi, Set<? extends Element> elements)
   {
      this.shadowApi = shadowApi;
      this.shadows = elements
            .stream()
            .map(element -> shadowApi.getShadowFactory().<Shadow<TypeMirror>>shadowFromElement(element))
            .collect(toUnmodifiableSet());
   }

   @Override
   public Set<Shadow<TypeMirror>> all()
   {
      return new HashSet<>(shadows);
   }

   @Override
   public Set<Declared> declaredTypes()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalDeclared());
   }

   @Override
   public Set<Class> classes()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalClass());
   }

   @Override
   public Set<Enum> enums()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalEnum());
   }

   @Override
   public Set<Interface> interfaces()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalInterface());
   }

   @Override
   public Set<Field> fields()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalField());
   }

   @Override
   public Set<Parameter> parameters()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalParameter());
   }

   @Override
   public Set<Method> methods()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalMethod());
   }

   @Override
   public Set<Constructor> constructors()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalConstructor());
   }

   @Override
   public Set<Annotation> annotations()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalAnnotation());
   }

   @Override
   public Set<Package> packages()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalPackage());
   }

   @Override
   public Set<Generic> generics()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalGeneric());
   }

   @Override
   public Set<Module> modules()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toOptionalModule());
   }

   private <SHADOW> Set<SHADOW> findShadows(Function<? super Shadow<TypeMirror>, Optional<SHADOW>> mapper)
   {
      return shadows.stream()
                    .map(mapper)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toUnmodifiableSet());
   }
}
