package io.determann.shadow.impl.test;

import javax.tools.FileObject;
import java.io.*;
import java.net.URI;

public class TestFileObject implements FileObject
{
   private final FileObject delegate;

   public TestFileObject(FileObject delegate) {this.delegate = delegate;}

   @Override
   public URI toUri()
   {
      return delegate.toUri();
   }

   @Override
   public String getName()
   {
      return delegate.getName();
   }

   @Override
   public InputStream openInputStream() throws IOException
   {
      return delegate.openInputStream();
   }

   @Override
   public OutputStream openOutputStream()
   {
      return new OutputStream()
      {
         @Override
         public void write(int b)
         {
            //noop
         }
      };
   }

   @Override
   public Reader openReader(boolean ignoreEncodingErrors) throws IOException
   {
      return delegate.openReader(ignoreEncodingErrors);
   }

   @Override
   public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException
   {
      return delegate.getCharContent(ignoreEncodingErrors);
   }

   @Override
   public Writer openWriter()
   {
      return new OutputStreamWriter(openOutputStream());
   }

   @Override
   public long getLastModified()
   {
      return delegate.getLastModified();
   }

   @Override
   public boolean delete()
   {
      return delegate.delete();
   }
}
