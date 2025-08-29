package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.NestingKind;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.ApContextImpl;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.query.Operations.MODIFIABLE_GET_MODIFIERS;
import static io.determann.shadow.api.query.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

public class DeclaredImpl extends TypeImpl<DeclaredType>
{
   private final TypeElement typeElement;

   DeclaredImpl(Ap.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
      this.typeElement = (TypeElement) declaredTypeMirror.asElement();
   }

   DeclaredImpl(Ap.Context context, TypeElement typeElement)
   {
      super(context, (DeclaredType) typeElement.asType());
      this.typeElement = typeElement;
   }

   public Set<Modifier> getModifiers()
   {
      return ApContextImpl.getModifiers(getElement());
   }

   public boolean isSubtypeOf(C.Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(getMirror(), adapt((Ap.Declared) type).toDeclaredType());
   }

   public TypeElement getElement()
   {
      return typeElement;
   }

   public NestingKind getNesting()
   {
      return switch (getElement().getNestingKind())
      {
         case MEMBER -> NestingKind.INNER;
         case TOP_LEVEL -> NestingKind.OUTER;
         default -> throw new IllegalStateException();
      };
   }

   public List<Ap.Field> getFields()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> element.getKind().equals(ElementKind.FIELD))
                         .map(VariableElement.class::cast)
                         .map(variableElement -> adapt(getApi(), variableElement))
                         .map(Ap.Field.class::cast)
                         .toList();
   }

   public List<Ap.Method> getMethods()
   {
      return ElementFilter.methodsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> adapt(getApi(), element))
                          .map(Ap.Method.class::cast)
                          .toList();
   }

   public List<Ap.Constructor> getConstructors()
   {
      return ElementFilter.constructorsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> adapt(getApi(), element))
                          .map(Ap.Constructor.class::cast)
                          .toList();
   }

   public List<Ap.Declared> getDirectSuperTypes()
   {
      return adapt(getApi()).toTypes()
                        .directSupertypes(getMirror())
                        .stream()
                        .map(typeMirror1 -> adapt(getApi(), ((DeclaredType) typeMirror1)))
                        .toList();
   }

   public Set<Ap.Declared> getSuperTypes()
   {
      return findAllSupertypes(new HashSet<>(), ((Ap.Declared) this));
   }

   private Set<Ap.Declared> findAllSupertypes(Set<Ap.Declared> found, Ap.Declared declared)
   {
      List<Ap.Declared> directSupertypes = declared.getDirectSuperTypes();
      found.addAll(directSupertypes);
      for (Ap.Declared directSupertype : directSupertypes)
      {
         findAllSupertypes(found, directSupertype);
      }
      return found;
   }

   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }

   public String getBinaryName()
   {
      return adapt(getApi()).toElements().getBinaryName(getElement()).toString();
   }

   public Ap.Array asArray()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getArrayType(getMirror()));
   }

   public Ap.Wildcard asExtendsWildcard()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getWildcardType(getMirror(), null));
   }

   public Ap.Wildcard asSuperWildcard()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getWildcardType(null, getMirror()));
   }

   public List<Ap.Interface> getDirectInterfaces()
   {
      return getElement().getInterfaces()
                         .stream()
                         .map(typeMirror -> adapt(getApi(), ((DeclaredType) typeMirror)))
                         .map(Ap.Interface.class::cast)
                         .toList();
   }

   public List<Ap.Interface> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> declared instanceof Ap.Interface)
                            .map(Ap.Interface.class::cast)
                            .toList();
   }

   public Ap.Package getPackage()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getPackageOf(getElement()));
   }

   public Ap.Module getModule()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getModuleOf(getElement()));
   }

   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   public String getJavaDoc()
   {
      return adapt(getApi()).toElements().getDocComment(getElement());
   }

   public List<Ap.AnnotationUsage> getAnnotationUsages()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   public List<Ap.AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getQualifiedName(),
                          getModifiers());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C.Declared otherDeclared))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), requestOrThrow(otherDeclared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME)) &&
             requestOrEmpty(otherDeclared, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false);
   }
}
