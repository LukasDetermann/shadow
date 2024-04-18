package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.query.NameableLangModel;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.TypeKind.RECORD_COMPONENT;
import static io.determann.shadow.api.lang_model.LangModelAdapter.generalize;
import static io.determann.shadow.meta_meta.Operations.NAME;
import static io.determann.shadow.meta_meta.Provider.request;

public class RecordComponentImpl extends ShadowImpl<TypeMirror> implements RecordComponent,
                                                                           NameableLangModel
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
      return LangModelAdapter.getTypes(getApi()).isSubtype(getMirror(), LangModelAdapter.particularType(shadow));
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isAssignable(getMirror(), LangModelAdapter.particularType(shadow));
   }

   @Override
   public Record getRecord()
   {
      return LangModelAdapter.generalize(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public Shadow getType()
   {
      return LangModelAdapter.generalize(getApi(), getElement().asType());
   }

   @Override
   public Method getGetter()
   {
      return (Method) LangModelAdapter.generalize(getApi(), getElement().getAccessor());
   }

   @Override
   public Package getPackage()
   {
      return LangModelAdapter
                     .generalize(getApi(), LangModelAdapter.getElements(getApi()).getPackageOf(getElement()));
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
      return LangModelAdapter.generalize(getApi(), LangModelAdapter.getElements(getApi()).getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
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
      return request(otherRecordComponent, NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), otherRecordComponent.getType());
   }
}
