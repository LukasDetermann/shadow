package org.determann.shadow.api;

import javax.lang.model.type.TypeMirror;


/**
 * The {@link ShadowApi} is transient to the java annotation processor api. meaning you can get the api used my this api.
 * in this case {@link TypeMirror }
 */
public interface MirrorBacked<MIRROR extends TypeMirror>
{
   @JdkApi
   MIRROR getMirror();
}
