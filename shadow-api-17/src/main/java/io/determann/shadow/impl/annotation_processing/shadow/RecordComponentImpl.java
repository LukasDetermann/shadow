package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.TypeKind.RECORD_COMPONENT;

public class RecordComponentImpl extends ShadowImpl<TypeMirror> implements RecordComponent
{
   private final RecordComponentElement recordComponentElement;

   public RecordComponentImpl(AnnotationProcessingContext annotationProcessingContext, RecordComponentElement recordComponentElement)
   {
      super(annotationProcessingContext, recordComponentElement.asType());
      this.recordComponentElement = recordComponentElement;
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return MirrorAdapter.getModifiers(getElement());
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().isSubtype(getMirror(), MirrorAdapter.getType(shadow));
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().isAssignable(getMirror(), MirrorAdapter.getType(shadow));
   }

   @Override
   public Record getRecord()
   {
      return MirrorAdapter.getShadow(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public Shadow getType()
   {
      return MirrorAdapter.getShadow(getApi(), getElement().asType());
   }

   @Override
   public Method getGetter()
   {
      return MirrorAdapter.getShadow(getApi(), getElement().getAccessor());
   }

   @Override
   public Package getPackage()
   {
      return MirrorAdapter
                     .getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getPackageOf(getElement()));
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
      return MirrorAdapter.getModule(getApi(), getElement());
   }

   @Override
   public String getSimpleName()
   {
      return MirrorAdapter.getSimpleName(getElement());
   }

   @Override
   public String getJavaDoc()
   {
      return MirrorAdapter.getJavaDoc(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return MirrorAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return MirrorAdapter.getDirectAnnotationUsages(getApi(), getElement());
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
