package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.recordComponent;

public class RecordComponentImpl implements Ap.RecordComponent
{
   private final RecordComponentElement recordComponentElement;
   private final Ap.Context context;
   private final TypeMirror typeMirror;

   public RecordComponentImpl(Ap.Context context, RecordComponentElement recordComponentElement)
   {
      this.typeMirror = recordComponentElement.asType();
      this.context = context;
      this.recordComponentElement = recordComponentElement;
   }

   @Override
   public boolean isSubtypeOf(Ap.Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(getMirror(), adapt(type).toTypeMirror());
   }

   @Override
   public boolean isAssignableFrom(Ap.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt(type).toTypeMirror());
   }

   @Override
   public Ap.Record getRecord()
   {
      return (Ap.Record) adapt(getApi(), ((TypeElement) getElement().getEnclosingElement()));
   }

   @Override
   public Ap.Type getType()
   {
      return adapt(getApi(), getElement().asType());
   }

   @Override
   public Ap.Method getGetter()
   {
      return (Ap.Method) adapt(getApi(), getElement().getAccessor());
   }

   public RecordComponentElement getElement()
   {
      return recordComponentElement;
   }

   @Override
   public Ap.Module getModule()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public List<Ap.AnnotationUsage> getAnnotationUsages()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<Ap.AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return recordComponent().annotate(getDirectAnnotationUsages())
                              .type(getType())
                              .name(getName())
                              .renderDeclaration(renderingContext);
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
      if (!(other instanceof Ap.RecordComponent otherRecordComponent))
      {
         return false;
      }
      return Objects.equals(getName(), otherRecordComponent.getName()) &&
             Objects.equals(getType(), otherRecordComponent.getType());
   }

   public TypeMirror getMirror()
   {
      return typeMirror;
   }

   public Ap.Context getApi()
   {
      return context;
   }
}
