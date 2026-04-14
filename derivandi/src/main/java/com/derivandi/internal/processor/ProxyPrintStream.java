package com.derivandi.internal.processor;

import com.derivandi.api.processor.SimpleContext;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ProxyPrintStream
      extends PrintStream
{
   private final BiConsumer<SimpleContext, String> systemOutHandler;
   private final Supplier<? extends SimpleContext> contextSupplier;

   public ProxyPrintStream(OutputStream out,
                           Charset charset,
                           BiConsumer<SimpleContext, String> systemOutHandler,
                           Supplier<? extends SimpleContext> contextSupplier)
   {
      super(out, false, charset);
      this.systemOutHandler = systemOutHandler;
      this.contextSupplier = contextSupplier;
   }

   @Override
   public void println(String x)
   {
      super.println(x);
      if (x != null)
      {
         systemOutHandler.accept(contextSupplier.get(), x);
      }
   }
}
