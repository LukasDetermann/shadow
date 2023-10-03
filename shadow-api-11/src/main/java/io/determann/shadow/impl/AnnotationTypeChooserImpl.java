package io.determann.shadow.impl;

import io.determann.shadow.api.AnnotationTypeChooser;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.Element;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static io.determann.shadow.api.ShadowApi.convert;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class AnnotationTypeChooserImpl implements AnnotationTypeChooser
{
   private final Set<Shadow> shadows;

   AnnotationTypeChooserImpl(ShadowApi shadowApi, Set<? extends Element> elements)
   {
      this.shadows = elements
            .stream()
            .map(element -> shadowApi.getShadowFactory().<Shadow>shadowFromElement(element))
            .collect(toUnmodifiableSet());
   }

   @Override
   public Set<Shadow> all()
   {
      return new HashSet<>(shadows);
   }

   @Override
   public Set<Declared> declaredTypes()
   {
      return findShadows(shadow -> convert(shadow).toDeclared());
   }

   @Override
   public Set<Class> classes()
   {
      return findShadows(shadow -> convert(shadow).toClass());
   }

   @Override
   public Set<Enum> enums()
   {
      return findShadows(shadow -> convert(shadow).toEnum());
   }

   @Override
   public Set<Interface> interfaces()
   {
      return findShadows(shadow -> convert(shadow).toInterface());
   }

   @Override
   public Set<Field> fields()
   {
      return findShadows(shadow -> convert(shadow).toField());
   }

   @Override
   public Set<Parameter> parameters()
   {
      return findShadows(shadow -> convert(shadow).toParameter());
   }

   @Override
   public Set<Method> methods()
   {
      return findShadows(shadow -> convert(shadow).toMethod());
   }

   @Override
   public Set<Constructor> constructors()
   {
      return findShadows(shadow -> convert(shadow).toConstructor());
   }

   @Override
   public Set<Annotation> annotations()
   {
      return findShadows(shadow -> convert(shadow).toAnnotation());
   }

   @Override
   public Set<Package> packages()
   {
      return findShadows(shadow -> convert(shadow).toPackage());
   }

   @Override
   public Set<Generic> generics()
   {
      return findShadows(shadow -> convert(shadow).toGeneric());
   }

   @Override
   public Set<Module> modules()
   {
      return findShadows(shadow -> convert(shadow).toModule());
   }

   private <SHADOW> Set<SHADOW> findShadows(Function<? super Shadow, Optional<SHADOW>> mapper)
   {
      return shadows.stream()
                    .map(mapper)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toUnmodifiableSet());
   }
}
