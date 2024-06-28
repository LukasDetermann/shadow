package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.structure.VariableLangModel;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.Variable;
import io.determann.shadow.api.shadow.type.Shadow;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.lang_model.LangModelAdapter.*;
import static io.determann.shadow.api.shadow.Operations.NAMEABLE_NAME;
import static io.determann.shadow.api.shadow.Operations.VARIABLE_GET_TYPE;
import static io.determann.shadow.api.shadow.Provider.request;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public abstract class VariableImpl extends ShadowImpl<TypeMirror> implements VariableLangModel
{
   private final VariableElement variableElement;

   protected VariableImpl(LangModelContext context, VariableElement variableElement)
   {
      super(context, variableElement.asType());
      this.variableElement = variableElement;
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return LangModelAdapter.getModifiers(getElement());
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isSubtype(particularType(shadow), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return getTypes(getApi()).isAssignable(particularType(shadow), getMirror());
   }

   @Override
   public Shadow getType()
   {
      return generalize(getApi(), getElement().asType());
   }

   @Override
   public Package getPackage()
   {
      return generalizePackage(getApi(), LangModelAdapter.getElements(getApi()).getPackageOf(getElement()));
   }

   public VariableElement getElement()
   {
      return variableElement;
   }

   @Override
   public TypeKind getKind()
   {
      return switch (getElement().getKind())
      {
         case ENUM_CONSTANT -> TypeKind.ENUM_CONSTANT;
         case FIELD -> TypeKind.FIELD;
         case PARAMETER -> TypeKind.PARAMETER;
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public Module getModule()
   {
      return generalize(getApi(), getElements(getApi()).getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public String getJavaDoc()
   {
      return getElements(getApi()).getDocComment(getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return generalize(getApi(), getElements(getApi()).getAllAnnotationMirrors(getElement()));
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
      return Objects.hash(getName(),
                          getType(),
                          getModifiers());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Variable otherVariable))
      {
         return false;
      }
      return request(otherVariable, NAMEABLE_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), requestOrThrow(otherVariable, VARIABLE_GET_TYPE)) &&
             Objects.equals(getModifiers(), otherVariable.getModifiers());
   }
}
