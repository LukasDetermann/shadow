package com.derivandi.internal.shadow.type;

import com.derivandi.api.Ap;
import com.derivandi.api.Origin;
import com.derivandi.api.dsl.RenderingContext;
import com.derivandi.api.dsl.generic.GenericAndExtendsStep;
import com.derivandi.api.dsl.generic.GenericExtendsStep;
import com.derivandi.api.dsl.interface_.InterfaceRenderable;
import com.derivandi.api.processor.SimpleContext;

import javax.lang.model.type.IntersectionType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.derivandi.api.adapter.Adapters.adapt;
import static com.derivandi.api.dsl.JavaDsl.generic;

public class IntersectionImpl extends TypeImpl<IntersectionType> implements Ap.Generic
{
   public IntersectionImpl(SimpleContext context, IntersectionType intersectionType)
   {
      super(context, intersectionType);
   }

   @Override
   public Ap.Type getBound()
   {
      return getBounds().getFirst();
   }

   @Override
   public List<Ap.Type> getBounds()
   {
      return getMirror().getBounds().stream()
                        .map(typeMirror -> adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<Ap.Interface> getAdditionalBounds()
   {
      List<Ap.Type> bounds = getBounds();
      if (bounds.size() <= 1)
      {
         return Collections.emptyList();
      }
      return bounds.stream().skip(1)
                   .map(Ap.Interface.class::cast)
                   .toList();
   }

   @Override
   public Optional<Ap.Type> getSuper()
   {
      return Optional.empty();
   }

   @Override
   public Object getEnclosing()
   {
      throw new IllegalStateException("can not get the enclosing Object for a generic backed by an intersection type");
   }

   @Override
   public Origin getOrigin()
   {
      return Origin.SOURCE_DECLARED;
   }

   @Override
   public Ap.Generic erasure()
   {
      return (Ap.Generic) adapt(getApi(), adapt(getApi()).toTypes().erasure(getMirror()));
   }

   @Override
   public List<Ap.AnnotationUsage> getAnnotationUsages()
   {
      return Collections.emptyList();
   }

   @Override
   public List<Ap.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Collections.emptyList();
   }

   @Override
   public String getName()
   {
      return "";
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      GenericExtendsStep extendsStep = generic().name(getName());

      List<Ap.Type> bounds = getBounds();
      if (bounds.isEmpty())
      {
         return extendsStep.renderDeclaration(renderingContext);
      }
      GenericAndExtendsStep andExtendsStep = switch (bounds.getFirst())
      {
         case Ap.Class cClass -> extendsStep.extends_(cClass);
         case Ap.Interface cInterface -> extendsStep.extends_(cInterface);
         case Ap.Generic generic -> extendsStep.extends_(generic);
         default -> throw new IllegalStateException();
      };
      if (bounds.size() == 1)
      {
         return andExtendsStep.renderDeclaration(renderingContext);
      }

      for (Ap.Type additionalBound : bounds.subList(1, bounds.size()))
      {
         andExtendsStep = andExtendsStep.extends_(((InterfaceRenderable) additionalBound));
      }
      return extendsStep.renderDeclaration(renderingContext);
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      return renderName(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return getName();
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Ap.Generic generic &&
             Objects.equals(getName(), generic.getName()) &&
             Objects.equals(getBounds(), generic.getBounds());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(), getBounds());
   }

   @Override
   public String toString()
   {
      return "Generic{" +
             "name='" + getName()  + '\'' +
             ", bounds=" + getBounds() +
             '}';
   }
}
