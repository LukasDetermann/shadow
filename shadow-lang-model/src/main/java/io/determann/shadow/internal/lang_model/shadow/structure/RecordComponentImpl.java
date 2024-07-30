package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.structure.RecordComponentLangModel;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.internal.lang_model.shadow.type.ShadowImpl;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.lang_model.LangModelAdapter.*;
import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Operations.RECORD_COMPONENT_GET_TYPE;
import static io.determann.shadow.api.shadow.TypeKind.RECORD_COMPONENT;

public class RecordComponentImpl extends ShadowImpl<TypeMirror> implements RecordComponentLangModel
{
   private final RecordComponentElement recordComponentElement;

   public RecordComponentImpl(LangModelContext context, RecordComponentElement recordComponentElement)
   {
      super(context, recordComponentElement.asType());
      this.recordComponentElement = recordComponentElement;
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return getTypes(getApi()).isSubtype(getMirror(), particularType(shadow));
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return getTypes(getApi()).isAssignable(getMirror(), particularType(shadow));
   }

   @Override
   public Record getRecord()
   {
      return generalize(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public Shadow getType()
   {
      return generalize(getApi(), getElement().asType());
   }

   @Override
   public Method getGetter()
   {
      return (Method) generalize(getApi(), getElement().getAccessor());
   }

   @Override
   public Package getPackage()
   {
      return generalizePackage(getApi(), getElements(getApi()).getPackageOf(getElement()));
   }

   public RecordComponentElement getElement()
   {
      return recordComponentElement;
   }

   @Override
   public TypeKind getKind()
   {
      return RECORD_COMPONENT;
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
      return Objects.hash(getName(), getType());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof RecordComponent otherRecordComponent))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherRecordComponent, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Provider.requestOrEmpty(otherRecordComponent, RECORD_COMPONENT_GET_TYPE).map(shadow -> Objects.equals(shadow, getType())).orElse(false);
   }
}
