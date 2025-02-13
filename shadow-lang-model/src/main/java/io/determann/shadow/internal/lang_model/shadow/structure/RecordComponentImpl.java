package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Method;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_RecordComponent;
import io.determann.shadow.api.lang_model.shadow.type.LM_Record;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.type.C_Type;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Operations.RECORD_COMPONENT_GET_TYPE;
import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;

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
      return adapt(getApi()).toTypes().isSubtype(getMirror(), adapt((LM_Type) type).toTypeMirror());
   }

   @Override
   public boolean isAssignableFrom(C_Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt((LM_Type) type).toTypeMirror());
   }

   @Override
   public LM_Record getRecord()
   {
      return (LM_Record) adapt(getApi(), ((TypeElement) getElement().getEnclosingElement()));
   }

   @Override
   public LM_Type getType()
   {
      return adapt(getApi(), getElement().asType());
   }

   @Override
   public LM_Method getGetter()
   {
      return (LM_Method) adapt(getApi(), getElement().getAccessor());
   }

   public RecordComponentElement getElement()
   {
      return recordComponentElement;
   }

   @Override
   public LM_Module getModule()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public List<LM_AnnotationUsage> getAnnotationUsages()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<LM_AnnotationUsage> getDirectAnnotationUsages()
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
   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }
}
