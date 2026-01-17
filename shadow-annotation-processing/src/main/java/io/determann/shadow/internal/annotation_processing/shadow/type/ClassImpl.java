package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.adapter.TypeAdapter;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.class_.ClassGenericStep;
import io.determann.shadow.internal.annotation_processing.shadow.structure.PropertyFactory;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.class_;
import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.innerClass;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class ClassImpl
      extends DeclaredImpl
      implements Ap.Class
{
   public ClassImpl(Ap.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public ClassImpl(Ap.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean isAssignableFrom(Ap.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt(type).toTypeMirror());
   }

   @Nullable
   @Override
   public Ap.Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return (Ap.Class) adapt(getApi(), ((DeclaredType) superclass));
   }

   @Override
   public List<Ap.Class> getPermittedSubClasses()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> adapt(getApi(), ((DeclaredType) typeMirror)))
                         .map(Ap.Class.class::cast)
                         .toList();
   }

   @Override
   public List<Ap.Property> getProperties()
   {
      return PropertyFactory.of(this);
   }

   @Override
   public List<Ap.Type> getGenericUsages()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> Adapters.<Ap.Type>adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<Ap.Generic> getGenericDeclarations()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> adapt(getApi(), element))
                         .toList();
   }

   @Override
   public Ap.Class withGenerics(Ap.Type... generics)
   {
      if (generics.length == 0 || getGenericDeclarations().size() != generics.length)
      {
         throw new IllegalArgumentException(getQualifiedName() +
                                            " has " +
                                            getGenericDeclarations().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      Optional<Ap.Declared> outerType = getSurrounding();
      if (!isStatic() && outerType.isPresent() &&
          (outerType.get() instanceof Ap.Interface anInterface &&
           !anInterface.getGenericDeclarations().isEmpty() ||
           outerType.get() instanceof Ap.Class aClass1 && !aClass1.getGenericDeclarations().isEmpty()))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            getQualifiedName() +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(Adapters::adapt)
            .map(TypeAdapter::toTypeMirror)
            .toArray(TypeMirror[]::new);

      return (Ap.Class) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public Ap.Class withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(name -> getApi().getDeclaredOrThrow(name))
                                .toArray(Ap.Type[]::new));
   }

   @Override
   public Ap.Class interpolateGenerics()
   {
      return (Ap.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public Ap.Primitive asUnboxed()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().unboxedType(getMirror()));
   }

   @Override
   public Ap.Class erasure()
   {
      return (Ap.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      ClassGenericStep extendsStep = (switch (getNesting())
                                      {
                                         case OUTER -> class_().package_(getPackage());
                                         case INNER -> innerClass().outer(getSurrounding().orElseThrow());
                                      }).annotate(getDirectAnnotationUsages())
                                        .modifier(getModifiers())
                                        .name(getName())
                                        .genericDeclaration(getGenericDeclarations())
                                        .genericUsage(getGenericUsages());

      return ofNullable(getSuperClass())
            .map(extendsStep::extends_)
            .orElse(extendsStep)
            .implements_(getDirectInterfaces())
            .permits(getPermittedSubClasses())
            .field(getFields())
            .method(getMethods())
            .constructor(getConstructors())
            .renderDeclaration(renderingContext);
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      return getQualifiedName();
   }

   @Override
   public String renderSimpleName(RenderingContext renderingContext)
   {
      return getName();
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      return (switch (getNesting())
              {
                 case OUTER -> class_().package_(getPackage());
                 case INNER -> innerClass().outer(getSurrounding().orElseThrow());
              }).name(getName())
                .genericDeclaration(getGenericDeclarations())
                .genericUsage(getGenericUsages())
                .renderType(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return renderingContext.renderName(renderQualifiedName(renderingContext));
   }

   @Override
   public boolean equals(Object other)
   {
      return equals(Ap.Class.class, other);
   }

   @Override
   public String toString()
   {
      return toString("Class");
   }
}
