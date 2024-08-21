package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.shadow.type.C_Primitive;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;

class PrimitiveRendererTest
{
   @Test
   void type()
   {
      ConsistencyTest.<C_Primitive>compileTime(context -> context.getConstants().getPrimitiveBoolean())
                     .runtime(stringClassFunction -> R_Adapter.generalize(boolean.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<C_Primitive>compileTime(context -> context.getConstants().getPrimitiveByte())
                     .runtime(stringClassFunction -> R_Adapter.generalize(byte.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<C_Primitive>compileTime(context -> context.getConstants().getPrimitiveShort())
                     .runtime(stringClassFunction -> R_Adapter.generalize(short.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<C_Primitive>compileTime(context -> context.getConstants().getPrimitiveInt())
                     .runtime(stringClassFunction -> R_Adapter.generalize(int.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<C_Primitive>compileTime(context -> context.getConstants().getPrimitiveLong())
                     .runtime(stringClassFunction -> R_Adapter.generalize(long.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<C_Primitive>compileTime(context -> context.getConstants().getPrimitiveChar())
                     .runtime(stringClassFunction -> R_Adapter.generalize(char.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<C_Primitive>compileTime(context -> context.getConstants().getPrimitiveFloat())
                     .runtime(stringClassFunction -> R_Adapter.generalize(float.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<C_Primitive>compileTime(context -> context.getConstants().getPrimitiveDouble())
                     .runtime(stringClassFunction -> R_Adapter.generalize(double.class))
                     .test(aClass -> render(DEFAULT, aClass).type());
   }
}