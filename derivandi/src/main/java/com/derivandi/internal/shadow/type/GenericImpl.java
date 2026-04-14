package com.derivandi.internal.shadow.type;

import com.derivandi.api.Ap;
import com.derivandi.api.Origin;
import com.derivandi.api.dsl.RenderingContext;
import com.derivandi.api.dsl.generic.GenericAndExtendsStep;
import com.derivandi.api.dsl.generic.GenericExtendsStep;
import com.derivandi.api.dsl.interface_.InterfaceRenderable;
import com.derivandi.api.processor.SimpleContext;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.derivandi.api.adapter.Adapters.adapt;
import static com.derivandi.api.dsl.JavaDsl.generic;

public class GenericImpl extends TypeImpl<TypeVariable> implements Ap.Generic
{
   private final TypeParameterElement typeParameterElement;

   public GenericImpl(SimpleContext context, TypeParameterElement typeParameterElement)
   {
      super(context, ((TypeVariable) typeParameterElement.asType()));
      this.typeParameterElement = typeParameterElement;
   }

   public GenericImpl(SimpleContext context, TypeVariable typeMirror)
   {
      super(context, typeMirror);
      this.typeParameterElement = (TypeParameterElement) adapt(getApi()).toTypes().asElement(typeMirror);
   }

   @Override
   public Ap.Type getBound()
   {
      return getBounds().getFirst();
   }

   @Override
   public List<Ap.Type> getBounds()
   {
      TypeMirror upperBound = getMirror().getUpperBound();
      if (upperBound instanceof IntersectionType intersectionType)
      {
         return intersectionType.getBounds().stream()
                                .map(typeMirror -> adapt(getApi(), typeMirror))
                                .toList();
      }
      return Collections.singletonList(adapt(getApi(), upperBound));
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
      TypeMirror lowerBound = getMirror().getLowerBound();
      if (lowerBound == null || lowerBound.getKind().equals(TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(adapt(getApi(), lowerBound));
   }

   @Override
   public Object getEnclosing()
   {
      return adapt(getApi(), getElement().getGenericElement());
   }

   @Override
   public Origin getOrigin()
   {
      return adapt(adapt(getApi()).toElements().getOrigin(getElement()));
   }

   public TypeParameterElement getElement()
   {
      return typeParameterElement;
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public List<Ap.AnnotationUsage> getAnnotationUsages()
   {
      return adapt(getApi(), getElement(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<Ap.AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement(), getElement().getAnnotationMirrors());
   }

   @Override
   public Ap.Generic erasure()
   {
      return (Ap.Generic) adapt(getApi(), adapt(getApi()).toTypes().erasure(getMirror()));
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
             "name='" + getName() + '\'' +
             ", bounds=" + getBounds() +
             '}';
   }
}
