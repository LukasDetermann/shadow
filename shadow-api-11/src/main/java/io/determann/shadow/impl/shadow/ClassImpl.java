package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.property.ImmutableProperty;
import io.determann.shadow.api.property.MutableProperty;
import io.determann.shadow.api.property.Property;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.property.ImmutablePropertyImpl;
import io.determann.shadow.impl.property.MutablePropertyImpl;
import io.determann.shadow.impl.property.PropertyImpl;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.ShadowApi.convert;
import static java.util.stream.Collectors.toUnmodifiableList;

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
   public boolean isAssignableFrom(Shadow shadow)
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().isAssignable(getMirror(), MirrorAdapter.getType(shadow));
   }

   @Override
   public Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (javax.lang.model.type.TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return MirrorAdapter.getShadow(getApi(), superclass);
   }

   @Override
   public List<Property> getProperties()
   {
      return PropertyImpl.of(this);
   }

   @Override
   public List<MutableProperty> getMutableProperties()
   {
      return MutablePropertyImpl.of(this);
   }

   @Override
   public List<ImmutableProperty> getImmutableProperties()
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
      return Optional.of(MirrorAdapter.getShadow(getApi(), enclosingType));
   }

   @Override
   public final Class withGenerics(Shadow... generics)
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
      if (getOuterType().flatMap(typeMirrorShadow -> convert(typeMirrorShadow)
                              .toInterface()
                              .map(anInterface -> !anInterface.getFormalGenerics().isEmpty())
                              .or(() -> convert(typeMirrorShadow).toClass().map(aClass -> !aClass.getGenerics().isEmpty())))
                        .orElse(false))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            getQualifiedName() +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = Arrays.stream(generics)
                                       .map(MirrorAdapter::getType)
                                       .toArray(TypeMirror[]::new);

      return MirrorAdapter
                     .getShadow(getApi(),
                                MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public Class withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(Arrays.stream(qualifiedGenerics)
                                .map(qualifiedName -> getApi().getDeclaredOrThrow(qualifiedName))
                                .toArray(Shadow[]::new));
   }

   @Override
   public List<Shadow> getGenerics()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> MirrorAdapter.<Shadow>getShadow(getApi(), typeMirror))
                        .collect(toUnmodifiableList());
   }

   @Override
   public List<Generic> getFormalGenerics()// formal
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> MirrorAdapter.<Generic>getShadow(getApi(), element))
                         .collect(toUnmodifiableList());
   }

   @Override
   public Class interpolateGenerics()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().capture(getMirror()));
   }

   @Override
   public Class erasure()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().erasure(getMirror()));
   }

   @Override
   public Primitive asUnboxed()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().unboxedType(getMirror()));
   }
}
