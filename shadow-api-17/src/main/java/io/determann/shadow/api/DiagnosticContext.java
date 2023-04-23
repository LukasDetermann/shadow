package io.determann.shadow.api;

import java.time.Instant;
import java.util.Objects;

public class DiagnosticContext
{
   private final ShadowApi api;
   private final String processorName;
   private final int processingRound;
   private final Instant start;
   private final Instant end;

   DiagnosticContext(ShadowApi api, String processorName, int processingRound, Instant start, Instant end)
   {
      this.api = api;
      this.processorName = processorName;
      this.processingRound = processingRound;
      this.start = start;
      this.end = end;
   }

   public ShadowApi getApi()
   {
      return api;
   }

   public String getProcessorName()
   {
      return processorName;
   }

   public int getProcessingRound()
   {
      return processingRound;
   }

   public Instant getStart()
   {
      return start;
   }

   public Instant getEnd()
   {
      return end;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof DiagnosticContext))
      {
         return false;
      }
      DiagnosticContext context = (DiagnosticContext) o;
      return getProcessingRound() == context.getProcessingRound() &&
             Objects.equals(getApi(), context.getApi()) &&
             Objects.equals(getProcessorName(), context.getProcessorName()) &&
             Objects.equals(getStart(), context.getStart()) &&
             Objects.equals(getEnd(), context.getEnd());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getApi(), getProcessorName(), getProcessingRound(), getStart(), getEnd());
   }

   @Override
   public String toString()
   {
      return "DiagnosticContext{" +
             "api=" + api +
             ", processorName='" + processorName + '\'' +
             ", processingRound=" + processingRound +
             ", start=" + start +
             ", now=" + end +
             '}';
   }
}
