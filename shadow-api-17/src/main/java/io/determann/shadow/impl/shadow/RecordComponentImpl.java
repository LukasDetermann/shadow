package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.type.TypeMirror;
import java.util.Objects;

import static io.determann.shadow.api.TypeKind.RECORD_COMPONENT;

public class RecordComponentImpl extends ShadowImpl<TypeMirror> implements RecordComponent
{
   private final RecordComponentElement recordComponentElement;

   public RecordComponentImpl(ShadowApi shadowApi, RecordComponentElement recordComponentElement)
   {
      super(shadowApi, recordComponentElement.asType());
      this.recordComponentElement = recordComponentElement;
   }

   @Override
   public boolean isSubtypeOf(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().isSubtype(getMirror(), shadow.getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().isAssignable(getMirror(), shadow.getMirror());
   }

   @Override
   public Record getRecord()
   {
      return getApi().getShadowFactory().shadowFromElement(getElement().getEnclosingElement());
   }

   @Override
   public Shadow<TypeMirror> getType()
   {
      return getApi().getShadowFactory().shadowFromType(getElement().asType());
   }

   @Override
   public Method getGetter()
   {
      return getApi().getShadowFactory().shadowFromElement(getElement().getAccessor());
   }

   @Override
   public Package getPackage()
   {
      return getApi().getShadowFactory().shadowFromElement(getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getPackageOf(getElement()));
   }

   @Override
   public RecordComponent erasure()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().erasure(getMirror()));
   }

   @Override
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
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getSimpleName(),
                          getRecord());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      RecordComponentImpl otherRecordComponent = (RecordComponentImpl) other;
      return Objects.equals(getSimpleName(), otherRecordComponent.getSimpleName()) &&
             Objects.equals(getRecord(), otherRecordComponent.getRecord());
   }
}
