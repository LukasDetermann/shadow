package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Variable;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Shadow;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Variable;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.internal.lang_model.shadow.type.ShadowImpl;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LM_Adapter.*;

public abstract class VariableImpl extends ShadowImpl<TypeMirror> implements LM_Variable
{
   private final VariableElement variableElement;

   protected VariableImpl(LM_Context context, VariableElement variableElement)
   {
      super(context, variableElement.asType());
      this.variableElement = variableElement;
   }

   @Override
   public Set<C_Modifier> getModifiers()
   {
      return LM_Adapter.getModifiers(getElement());
   }

   @Override
   public boolean isSubtypeOf(C_Shadow shadow)
   {
      return LM_Adapter.getTypes(getApi()).isSubtype(particularType((LM_Declared) shadow), getMirror());
   }

   @Override
   public boolean isAssignableFrom(C_Shadow shadow)
   {
      return getTypes(getApi()).isAssignable(particularType((LM_Declared) shadow), getMirror());
   }

   @Override
   public LM_Shadow getType()
   {
      return generalize(getApi(), getElement().asType());
   }

   @Override
   public LM_Package getPackage()
   {
      return generalizePackage(getApi(), LM_Adapter.getElements(getApi()).getPackageOf(getElement()));
   }

   public VariableElement getElement()
   {
      return variableElement;
   }

   @Override
   public C_TypeKind getKind()
   {
      return switch (getElement().getKind())
      {
         case ENUM_CONSTANT -> C_TypeKind.ENUM_CONSTANT;
         case FIELD -> C_TypeKind.FIELD;
         case PARAMETER -> C_TypeKind.PARAMETER;
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public LM_Module getModule()
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
   public List<LM_AnnotationUsage> getAnnotationUsages()
   {
      return generalize(getApi(), getElements(getApi()).getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<LM_AnnotationUsage> getDirectAnnotationUsages()
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
      if (!(other instanceof C_Variable otherVariable))
      {
         return false;
      }
      return requestOrEmpty(otherVariable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), requestOrThrow(otherVariable, VARIABLE_GET_TYPE)) &&
             requestOrEmpty(otherVariable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false);
   }
}
