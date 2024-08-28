package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.Provider;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Method;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.lang_model.shadow.structure.LM_RecordComponent;
import io.determann.shadow.api.lang_model.shadow.type.LM_Record;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.type.C_Type;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Operations.RECORD_COMPONENT_GET_TYPE;
import static io.determann.shadow.api.lang_model.LM_Adapter.*;
import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;

public class RecordComponentImpl implements LM_RecordComponent
{
   private final RecordComponentElement recordComponentElement;
   private final LM_Context context;
   private final TypeMirror typeMirror;

   public RecordComponentImpl(LM_Context context, RecordComponentElement recordComponentElement)
   {
      this.typeMirror = recordComponentElement.asType();
      this.context = context;
      this.recordComponentElement = recordComponentElement;
   }

   @Override
   public boolean isSubtypeOf(C_Type type)
   {
      return getTypes(getApi()).isSubtype(getMirror(), particularType((LM_Type) type));
   }

   @Override
   public boolean isAssignableFrom(C_Type type)
   {
      return getTypes(getApi()).isAssignable(getMirror(), particularType((LM_Type) type));
   }

   @Override
   public LM_Record getRecord()
   {
      return generalize(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public LM_Type getType()
   {
      return generalize(getApi(), getElement().asType());
   }

   @Override
   public LM_Method getGetter()
   {
      return (LM_Method) generalize(getApi(), getElement().getAccessor());
   }

   @Override
   public LM_Package getPackage()
   {
      return generalizePackage(getApi(), getElements(getApi()).getPackageOf(getElement()));
   }

   public RecordComponentElement getElement()
   {
      return recordComponentElement;
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
      return Objects.hash(getName(), getType());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C_RecordComponent otherRecordComponent))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherRecordComponent, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Provider.requestOrEmpty(otherRecordComponent, RECORD_COMPONENT_GET_TYPE).map(type -> Objects.equals(type, getType())).orElse(false);
   }

   public TypeMirror getMirror()
   {
      return typeMirror;
   }

   public LM_Context getApi()
   {
      return context;
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
