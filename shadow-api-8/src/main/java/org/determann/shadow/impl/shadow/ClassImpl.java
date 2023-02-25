package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.property.ImmutableProperty;
import org.determann.shadow.api.property.MutableProperty;
import org.determann.shadow.api.property.Property;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.*;
import org.determann.shadow.impl.property.ImmutablePropertyImpl;
import org.determann.shadow.impl.property.MutablePropertyImpl;
import org.determann.shadow.impl.property.PropertyImpl;
import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.determann.shadow.api.ShadowApi.convert;

public class ClassImpl extends DeclaredImpl implements Class
{
   public ClassImpl(ShadowApi shadowApi, DeclaredType declaredTypeMirror)
   {
      super(shadowApi, declaredTypeMirror);
   }

   public ClassImpl(ShadowApi shadowApi, TypeElement typeElement)
   {
      super(shadowApi, typeElement);
   }

   @Override
   public boolean isAssignableFrom(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().types().isAssignable(getMirror(), shadow.getMirror());
   }

   @Override
   public Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (javax.lang.model.type.TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return getApi().getShadowFactory().shadowFromType(superclass);
   }

   @Override
   public List<Property> getProperties()
   {
      return PropertyImpl.of(this);
   }

   @Override
   public @UnmodifiableView List<MutableProperty> getMutableProperties()
   {
      return MutablePropertyImpl.of(this);
   }

   @Override
   public @UnmodifiableView List<ImmutableProperty> getImmutableProperties()
   {
      return ImmutablePropertyImpl.of(this);
   }

   @Override
   public Optional<Declared> getOuterType()
   {
      TypeMirror enclosingType = getMirror().getEnclosingType();
      if (enclosingType.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(getApi().getShadowFactory().shadowFromType(enclosingType));
   }

   @Override
   @SafeVarargs
   public final Class withGenerics(Shadow<? extends TypeMirror>... generics)
   {
      if (generics.length == 0 || getFormalGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(getQualifiedName() +
                                            " has " +
                                            getFormalGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      if (getOuterType().map(typeMirrorShadow -> convert(typeMirrorShadow)
                              .toInterface()
                              .map(anInterface -> !anInterface.getFormalGenerics().isEmpty())
                              .orElseGet(() -> convert(typeMirrorShadow).toClass().map(aClass -> !aClass.getGenerics().isEmpty()).orElse(false)))
                        .orElse(false))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            getQualifiedName() +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = Arrays.stream(generics)
                                       .map(Shadow::getMirror)
                                       .toArray(TypeMirror[]::new);

      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public Class withGenerics(String... qualifiedGenerics)
   {
      //noinspection unchecked
      return withGenerics((Shadow[])Arrays.stream(qualifiedGenerics)
                                .map(qualifiedName -> getApi().getDeclaredOrThrow(qualifiedName))
                                .toArray(Shadow[]::new));
   }

   @Override
   public List<Shadow<TypeMirror>> getGenerics()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> getApi().getShadowFactory().<Shadow<TypeMirror>>shadowFromType(typeMirror))
                        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Generic> getFormalGenerics()// formal
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> getApi().getShadowFactory().<Generic>shadowFromElement(element))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Class interpolateGenerics()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().capture(getMirror()));
   }

   @Override
   public Primitive asUnboxed()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().unboxedType(getMirror()));
   }
}
