package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.NestingKind;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.DeclaredConverter;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.lang_model.LangModelAdapter.generalize;

public class DeclaredImpl extends ShadowImpl<DeclaredType> implements Annotation,
                                                                      Enum
{
   private final TypeElement typeElement;

   public DeclaredImpl(LangModelContext context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
      this.typeElement = (TypeElement) declaredTypeMirror.asElement();
   }

   public DeclaredImpl(LangModelContext context, TypeElement typeElement)
   {
      super(context, (DeclaredType) typeElement.asType());
      this.typeElement = typeElement;
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return LangModelAdapter.getModifiers(getElement());
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isSubtype(getMirror(), LangModelAdapter.particularType(shadow));
   }

   @Override
   public TypeKind getKind()
   {
      return switch (getElement().getKind())
      {
         case ENUM -> TypeKind.ENUM;
         case CLASS -> TypeKind.CLASS;
         case INTERFACE -> TypeKind.INTERFACE;
         case ANNOTATION_TYPE -> TypeKind.ANNOTATION;
         case RECORD -> TypeKind.RECORD;
         default -> throw new IllegalStateException();
      };
   }

   public TypeElement getElement()
   {
      return typeElement;
   }

   @Override
   public NestingKind getNesting()
   {
      return switch (getElement().getNestingKind())
      {
         case MEMBER -> NestingKind.INNER;
         case TOP_LEVEL -> NestingKind.OUTER;
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public List<Field> getFields()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> element.getKind().equals(ElementKind.FIELD))
                         .map(variableElement -> LangModelAdapter.<Field>generalize(getApi(), variableElement))
                         .toList();
   }

   @Override
   public List<Method> getMethods()
   {
      return ElementFilter.methodsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> LangModelAdapter.generalize(getApi(), element))
                          .map(Method.class::cast)
                          .toList();
   }

   @Override
   public List<Constructor> getConstructors()
   {
      return ElementFilter.constructorsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> LangModelAdapter.<Constructor>generalize(getApi(), element))
                          .map(Constructor.class::cast)
                          .toList();
   }

   @Override
   public List<Declared> getDirectSuperTypes()
   {
      return LangModelAdapter.getTypes(getApi())
                             .directSupertypes(getMirror())
                             .stream()
                             .map(typeMirror1 -> LangModelAdapter.<Declared>generalize(getApi(), typeMirror1))
                             .toList();
   }

   @Override
   public Set<Declared> getSuperTypes()
   {
      return findAllSupertypes(new HashSet<>(), this);
   }

   private Set<Declared> findAllSupertypes(Set<Declared> found, Declared declared)
   {
      List<Declared> directSupertypes = declared.getDirectSuperTypes();
      found.addAll(directSupertypes);
      for (Declared directSupertype : directSupertypes)
      {
         findAllSupertypes(found, directSupertype);
      }
      return found;
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }

   @Override
   public String getBinaryName()
   {
      return LangModelAdapter.getElements(getApi()).getBinaryName(getElement()).toString();
   }

   @Override
   public List<Interface> getDirectInterfaces()
   {
      return getElement().getInterfaces()
                         .stream()
                         .map(typeMirror -> LangModelAdapter.<Interface>generalize(getApi(), typeMirror))
                         .toList();
   }

   @Override
   public List<Interface> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> declared.getKind().equals(TypeKind.INTERFACE))
                            .map(Converter::convert)
                            .map(DeclaredConverter::toInterfaceOrThrow)
                            .toList();
   }

   @Override
   public List<EnumConstant> getEumConstants()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> element.getKind().equals(ElementKind.ENUM_CONSTANT))
                         .map(VariableElement.class::cast)
                         .map(variableElement -> LangModelAdapter.<EnumConstant>generalize(getApi(), variableElement))
                         .toList();
   }

   @Override
   public Package getPackage()
   {
      return LangModelAdapter
                     .generalize(getApi(), LangModelAdapter.getElements(getApi()).getPackageOf(getElement()));
   }

   @Override
   public Module getModule()
   {
      return LangModelAdapter.generalize(getApi(), LangModelAdapter.getElements(getApi()).getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public String getJavaDoc()
   {
      return LangModelAdapter.getElements(getApi()).getDocComment(getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return generalize(getApi(), LangModelAdapter.getElements(getApi()).getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return generalize(getApi(), getElement().getAnnotationMirrors());
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(),
                          getQualifiedName(),
                          getModifiers());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Declared otherDeclared))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), otherDeclared.getQualifiedName()) &&
             Objects.equals(getKind(), otherDeclared.getKind()) &&
             Objects.equals(getModifiers(), otherDeclared.getModifiers());
   }
}
