package com.derivandi.internal.annotationvalue;

import com.derivandi.api.Ap;
import com.derivandi.api.Origin;
import com.derivandi.api.dsl.RenderingContext;
import com.derivandi.api.dsl.annotation_usage.AnnotationUsageNameStep;
import com.derivandi.api.processor.SimpleContext;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.*;

import static com.derivandi.api.adapter.Adapters.adapt;
import static com.derivandi.api.dsl.JavaDsl.annotationUsage;
import static com.derivandi.internal.annotationvalue.AnnotationValueImpl.create;

public class AnnotationUsageImpl
      implements Ap.AnnotationUsage
{
   private final SimpleContext context;
   private final AnnotationMirror annotationMirror;
   private static AnnotatedConstruct annotated;

   public static List<Ap.AnnotationUsage> from(SimpleContext langModelContext,
                                               AnnotatedConstruct annotated,
                                               Collection<? extends AnnotationMirror> annotationMirrors)
   {
      AnnotationUsageImpl.annotated = annotated;
      return annotationMirrors.stream().map(annotationMirror -> from(langModelContext, annotationMirror)).toList();
   }

   static Ap.AnnotationUsage from(SimpleContext langModelContext, AnnotationMirror annotationMirror)
   {
      return new AnnotationUsageImpl(langModelContext, annotationMirror);
   }

   private AnnotationUsageImpl(SimpleContext context, AnnotationMirror annotationMirror)
   {
      this.context = context;
      this.annotationMirror = annotationMirror;
   }

   @Override
   public Map<Ap.Method, Ap.AnnotationValue> getValues()
   {
      Map<Ap.Method, Ap.AnnotationValue> result = new LinkedHashMap<>();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withoutDefaults = annotationMirror.getElementValues();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withDefaults =
            adapt(getApi()).toElements().getElementValuesWithDefaults(annotationMirror);

      for (Map.Entry<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> entry : withDefaults.entrySet())
      {
         result.put((Ap.Method) adapt(getApi(), entry.getKey()),
                    create(context, entry.getValue(), !withoutDefaults.containsKey(entry.getKey())));
      }
      return result;
   }

   @Override
   public Origin getOrigin()
   {
      return adapt(adapt(getApi()).toElements().getOrigin(annotated, getAnnotationMirror()));
   }

   @Override
   public Ap.Annotation getAnnotation()
   {
      return (Ap.Annotation) adapt(getApi(), annotationMirror.getAnnotationType());
   }

   @Override
   public String toString()
   {
      return annotationMirror.toString();
   }

   public AnnotationMirror getAnnotationMirror()
   {
      return annotationMirror;
   }

   public SimpleContext getApi()
   {
      return context;
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      AnnotationUsageNameStep nameStep = annotationUsage().type(getAnnotation());

      for (Map.Entry<? extends Ap.Method, ? extends Ap.AnnotationValue> entry : getValues().entrySet())
      {
         nameStep = nameStep.name(entry.getKey().getName()).value(entry.getValue());
      }
      return nameStep.renderDeclaration(renderingContext);
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getAnnotation(),
                          getValues());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Ap.AnnotationUsage otherAnnotationUsage))
      {
         return false;
      }
      return Objects.equals(getAnnotation(), otherAnnotationUsage.getAnnotation()) &&
             Objects.equals(getValues(), otherAnnotationUsage.getValues());
   }
}
