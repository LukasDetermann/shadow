package org.determann.shadow.impl;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.ShadowFactory;
import org.determann.shadow.api.shadow.AnnotationUsage;
import org.determann.shadow.api.shadow.Shadow;
import org.determann.shadow.impl.shadow.*;
import org.determann.shadow.impl.shadow.wraper.AnnotationUsageImpl;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.List;

@SuppressWarnings("unchecked")
class ShadowFactoryImpl implements ShadowFactory
{
   private final ShadowApi shadowApi;

   ShadowFactoryImpl(ShadowApi shadowApi)
   {
      this.shadowApi = shadowApi;
   }

   @Override
   public <SHADOW extends Shadow<? extends TypeMirror>> SHADOW shadowFromElement(Element element)
   {
      switch (element.getKind())
      {
         case PACKAGE:
            return (SHADOW) new PackageImpl(shadowApi, (PackageElement) element);
         case ENUM:
         case ANNOTATION_TYPE:
            return (SHADOW) new DeclaredImpl(shadowApi, (TypeElement) element);
         case CLASS:
            return (SHADOW) new ClassImpl(shadowApi, (TypeElement) element);
         case INTERFACE:
            return (SHADOW) new InterfaceImpl(shadowApi, (TypeElement) element);
         case METHOD:
         case CONSTRUCTOR:
            return (SHADOW) new ExecutableImpl(shadowApi, ((ExecutableElement) element));
         case ENUM_CONSTANT:
            return (SHADOW) new EnumConstantImpl(shadowApi, (VariableElement) element);
         case FIELD:
            return (SHADOW) new FieldImpl(shadowApi, (VariableElement) element);
         case PARAMETER:
            return (SHADOW) new ParameterImpl(shadowApi, (VariableElement) element);
         case TYPE_PARAMETER:
            return (SHADOW) new GenericImpl(shadowApi, (TypeParameterElement) element);
         case MODULE:
            return (SHADOW) new ModuleImpl(shadowApi, (ModuleElement) element);
         case OTHER:
         case STATIC_INIT:
         case INSTANCE_INIT:
         case EXCEPTION_PARAMETER:
         case RESOURCE_VARIABLE:
         case LOCAL_VARIABLE:
            throw new IllegalArgumentException("not implemented");
         default:
            throw new IllegalArgumentException();
      }
   }

   @Override
   public <SHADOW extends Shadow<? extends TypeMirror>> SHADOW shadowFromType(TypeMirror typeMirror)
   {
      switch (typeMirror.getKind())
      {
         case BOOLEAN:
         case BYTE:
         case SHORT:
         case INT:
         case LONG:
         case CHAR:
         case FLOAT:
         case DOUBLE:
            return(SHADOW) new PrimitiveImpl(shadowApi, (PrimitiveType) typeMirror);
         case ARRAY:
            return(SHADOW) new ArrayImpl(shadowApi, (ArrayType) typeMirror);
         case DECLARED:
            switch (shadowApi.getJdkApiContext().types().asElement(typeMirror).getKind())
            {
               case CLASS:
                  return(SHADOW) new ClassImpl(shadowApi, ((DeclaredType) typeMirror));
               case INTERFACE:
                  return(SHADOW) new InterfaceImpl(shadowApi, (DeclaredType) typeMirror);
               case ANNOTATION_TYPE:
               case ENUM:
                  return(SHADOW) new DeclaredImpl(shadowApi, (DeclaredType) typeMirror);
               default:
                  throw new IllegalArgumentException("not implemented");
            }
         case WILDCARD:
            return(SHADOW) new WildcardImpl(shadowApi, (WildcardType) typeMirror);
         case VOID:
            return (SHADOW)new VoidImpl(shadowApi, ((NoType) typeMirror));
         case PACKAGE:
            return(SHADOW) new PackageImpl(shadowApi, (NoType) typeMirror);
         case MODULE:
            return(SHADOW) new ModuleImpl(shadowApi, (NoType) typeMirror);
         case NULL:
            return(SHADOW) new NullImpl(shadowApi, (NullType) typeMirror);
         case TYPEVAR:
            return(SHADOW)new GenericImpl(shadowApi, ((TypeVariable) typeMirror));
         case INTERSECTION:
            return (SHADOW)new IntersectionImpl(shadowApi, ((IntersectionType) typeMirror));
         case EXECUTABLE:
         case NONE:
            throw new IllegalArgumentException("bug in this api: executables should be created for elements");
         case ERROR:
         case OTHER:
         case UNION:
            throw new IllegalArgumentException("not implemented");
         default:
            throw new IllegalArgumentException();
      }
   }

   @Override
   public List<AnnotationUsage> annotationUsages(List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(shadowApi, annotationMirrors);
   }
}
