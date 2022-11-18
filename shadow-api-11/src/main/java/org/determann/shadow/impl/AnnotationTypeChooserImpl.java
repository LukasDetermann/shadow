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
import static org.determann.shadow.api.ShadowApi.convert;

public class AnnotationTypeChooserImpl implements AnnotationTypeChooser
{
   private final Set<Shadow<TypeMirror>> shadows;

   AnnotationTypeChooserImpl(ShadowApi shadowApi, Set<? extends Element> elements)
   {
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

   private <SHADOW> Set<SHADOW> findShadows(Function<? super Shadow<TypeMirror>, Optional<SHADOW>> mapper)
   {
      return shadows.stream()
                    .map(mapper)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toUnmodifiableSet());
   }
}
