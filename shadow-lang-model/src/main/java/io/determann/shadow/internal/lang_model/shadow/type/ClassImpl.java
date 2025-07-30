package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.lang_model.adapter.TypeAdapter;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;
import io.determann.shadow.implementation.support.api.shadow.type.ClassSupport;
import io.determann.shadow.internal.lang_model.shadow.structure.PropertyImpl;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;
import static java.util.Arrays.stream;

public class ClassImpl extends DeclaredImpl implements LM.Class
{
   public ClassImpl(LM.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public ClassImpl(LM.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean isAssignableFrom(C.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt((LM.Type) type).toTypeMirror());
   }

   @Override
   public LM.Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return (LM.Class) adapt(getApi(), ((DeclaredType) superclass));
   }

   @Override
   public List<LM.Class> getPermittedSubClasses()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> adapt(getApi(), ((DeclaredType) typeMirror)))
                         .map(LM.Class.class::cast)
                         .toList();
   }

   @Override
   public List<LM.Property> getProperties()
   {
      return PropertySupport.propertiesOf(this)
                            .stream()
                            .map(delegate -> new PropertyImpl(getApi(), delegate))
                            .map(LM.Property.class::cast)
                            .toList();
   }

   @Override
   public Optional<LM.Declared> getOuterType()
   {
      TypeMirror enclosingType = getMirror().getEnclosingType();
      if (enclosingType.getKind().equals(TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(adapt(getApi(), ((DeclaredType) enclosingType)));
   }

   @Override
   public List<LM.Type> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> Adapters.<LM.Type>adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<LM.Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> Adapters.adapt(getApi(), element))
                         .toList();
   }

   @Override
   public LM.Class withGenerics(LM.Type... generics)
   {
      if (generics.length == 0 || getGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(getQualifiedName() +
                                            " has " +
                                            getGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      Optional<LM.Declared> outerType = getOuterType();
      if (outerType.isPresent() &&
          (outerType.get() instanceof LM.Interface anInterface &&
           !anInterface.getGenerics().isEmpty() ||
           outerType.get() instanceof LM.Class aClass1 && !aClass1.getGenerics().isEmpty()))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            getQualifiedName() +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(Adapters::adapt)
            .map(TypeAdapter::toTypeMirror)
            .toArray(TypeMirror[]::new);

      return (LM.Class) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public LM.Class withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(name -> getApi().getDeclaredOrThrow(name))
                                .toArray(LM.Type[]::new));
   }

   @Override
   public LM.Class interpolateGenerics()
   {
      return (LM.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public LM.Primitive asUnboxed()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().unboxedType(getMirror()));
   }

   @Override
   public LM.Class erasure()
   {
      return (LM.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
   }

   @Override
   public boolean equals(Object other)
   {
      return ClassSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return ClassSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return ClassSupport.toString(this);
   }
}
