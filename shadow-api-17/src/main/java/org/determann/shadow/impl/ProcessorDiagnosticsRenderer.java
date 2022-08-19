package org.determann.shadow.impl;

import java.time.Duration;

public class ProcessorDiagnosticsRenderer
{
   private ProcessorDiagnosticsRenderer()
   {
   }

   public static String render(String processorName, int processingRoundCount, Duration processingDuration)
   {
      return processorName + " took " + renderDuration(processingDuration) + " in round " + processingRoundCount + "\n";
   }

   private static String renderDuration(Duration processingDuration)
   {
      return processingDuration.toString()
                               .substring(2)
                               .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                               .toLowerCase();
   }
}
