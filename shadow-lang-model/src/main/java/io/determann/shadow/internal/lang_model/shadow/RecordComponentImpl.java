package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.TypeKind.RECORD_COMPONENT;

public class RecordComponentImpl extends ShadowImpl<TypeMirror> implements RecordComponent
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
      return LangModelAdapter.getTypes(getApi()).isSubtype(getMirror(), LangModelAdapter.getType(shadow));
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isAssignable(getMirror(), LangModelAdapter.getType(shadow));
   }

   @Override
   public Record getRecord()
   {
      return LangModelAdapter.getShadow(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public Shadow getType()
   {
      return LangModelAdapter.getShadow(getApi(), getElement().asType());
   }

   @Override
   public Method getGetter()
   {
      return LangModelAdapter.getShadow(getApi(), getElement().getAccessor());
   }

   @Override
   public Package getPackage()
   {
      return LangModelAdapter
                     .getShadow(getApi(), LangModelAdapter.getElements(getApi()).getPackageOf(getElement()));
   }

   public RecordComponentElement getElement()
   {
      return recordComponentElement;
   }

   @Override
   public TypeKind getTypeKind()
   {
      return RECORD_COMPONENT;
   }

   @Override
   public Module getModule()
   {
      return LangModelAdapter.getModule(getApi(), getElement());
   }

   @Override
   public String getName()
   {
      return LangModelAdapter.getName(getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return LangModelAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return LangModelAdapter.getDirectAnnotationUsages(getApi(), getElement());
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
      return Objects.equals(getName(), otherRecordComponent.getName()) &&
             Objects.equals(getType(), otherRecordComponent.getType());
   }
}
