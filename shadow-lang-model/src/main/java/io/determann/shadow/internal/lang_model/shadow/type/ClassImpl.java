package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.adapter.LM_TypeAdapter;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Property;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.lang_model.shadow.type.primitive.LM_Primitive;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;
import io.determann.shadow.implementation.support.api.shadow.type.ClassSupport;
import io.determann.shadow.internal.lang_model.shadow.structure.PropertyImpl;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;
import static java.util.Arrays.stream;

public class ClassImpl extends DeclaredImpl implements LM_Class
{
   public ClassImpl(LM_Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public ClassImpl(LM_Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean isAssignableFrom(C_Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt((LM_Type) type).toTypeMirror());
   }

   @Override
   public LM_Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return (LM_Class) adapt(getApi(), ((DeclaredType) superclass));
   }

   @Override
   public List<LM_Class> getPermittedSubClasses()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> adapt(getApi(), ((DeclaredType) typeMirror)))
                         .map(LM_Class.class::cast)
                         .toList();
   }

   @Override
   public List<LM_Property> getProperties()
   {
      return PropertySupport.propertiesOf(this)
                            .stream()
                            .map(delegate -> new PropertyImpl(getApi(), delegate))
                            .map(LM_Property.class::cast)
                            .toList();
   }

   @Override
   public Optional<LM_Declared> getOuterType()
   {
      TypeMirror enclosingType = getMirror().getEnclosingType();
      if (enclosingType.getKind().equals(TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(adapt(getApi(), ((DeclaredType) enclosingType)));
   }

   @Override
   public List<LM_Type> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> LM_Adapters.<LM_Type>adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<LM_Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LM_Adapters.adapt(getApi(), element))
                         .toList();
   }

   @Override
   public LM_Class withGenerics(LM_Type... generics)
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
      Optional<LM_Declared> outerType = getOuterType();
      if (outerType.isPresent() &&
          (outerType.get() instanceof LM_Interface anInterface &&
           !anInterface.getGenerics().isEmpty() ||
           outerType.get() instanceof LM_Class aClass1 && !aClass1.getGenerics().isEmpty()))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            getQualifiedName() +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(LM_Adapters::adapt)
            .map(LM_TypeAdapter::toTypeMirror)
            .toArray(TypeMirror[]::new);

      return (LM_Class) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public LM_Class withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(name -> getApi().getDeclaredOrThrow(name))
                                .toArray(LM_Type[]::new));
   }

   @Override
   public LM_Class interpolateGenerics()
   {
      return (LM_Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public LM_Primitive asUnboxed()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().unboxedType(getMirror()));
   }

   @Override
   public LM_Class erasure()
   {
      return (LM_Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
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
