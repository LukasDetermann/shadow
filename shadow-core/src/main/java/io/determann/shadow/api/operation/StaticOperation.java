package io.determann.shadow.api.operation;

public sealed interface StaticOperation<RESULT> extends Operation<RESULT> permits StaticOperation0,
                                                                                  StaticOperation1,
                                                                                  StaticOperation2 {}