package io.determann.shadow.api.query.operation;


public sealed interface InstanceOperation<TYPE, RESULT> extends Operation<RESULT> permits InstanceOperation0,
                                                                                          InstanceOperation1,
                                                                                          InstanceOperation2 {}