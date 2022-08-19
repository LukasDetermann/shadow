package org.determann.shadow.impl;

import org.determann.shadow.api.AnnotationTypeChooser;
import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.*;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            .collect(Collectors.toSet());
   }

   @Override
   public Set<Shadow<TypeMirror>> all()
   {
      return new HashSet<>(shadows);
   }

   @Override
   public Set<Declared> declaredTypes()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toDeclared());
   }

   @Override
   public Set<Class> classes()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toClass());
   }

   @Override
   public Set<Enum> enums()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toEnum());
   }

   @Override
   public Set<Interface> interfaces()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toInterface());
   }

   @Override
   public Set<Record> records()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toRecord());
   }

   @Override
   public Set<Field> fields()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toField());
   }

   @Override
   public Set<Parameter> parameters()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toParameter());
   }

   @Override
   public Set<Method> methods()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toMethod());
   }

   @Override
   public Set<Constructor> constructors()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toConstructor());
   }

   @Override
   public Set<Annotation> annotations()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toAnnotation());
   }

   @Override
   public Set<Package> packages()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toPackage());
   }

   @Override
   public Set<Generic> generics()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toGeneric());
   }

   @Override
   public Set<Module> modules()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toModule());
   }

   @Override
   public Set<RecordComponent> recordComponents()
   {
      return findShadows(shadow -> shadowApi.convert(shadow).toRecordComponent());
   }

   private <SHADOW> Set<SHADOW> findShadows(Function<? super Shadow<TypeMirror>, Optional<SHADOW>> mapper)
   {
      return shadows.stream()
                    .map(mapper)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
   }
}
