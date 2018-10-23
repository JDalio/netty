package com.mda.learn_netty.protocol.codec;

import org.jboss.marshalling.*;

import java.io.IOException;

public final class MarshallingCodecFactory
{
    //Marshaller is used to monitor the course of writing an obj
    protected static Marshaller buildMarshalling() throws IOException
    {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        Marshaller marshaller = marshallerFactory.createMarshaller(configuration);
        return marshaller;
    }

    protected static Unmarshaller buildUnMarshalling() throws IOException
    {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        //monitor the reading course
        final Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(configuration);
        return unmarshaller;
    }

}
