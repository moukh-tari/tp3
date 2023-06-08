package ma.service;

import io.grpc.stub.StreamObserver;
import ma.stubs.Bank;
import ma.stubs.BankserviceGrpc;

import java.util.Timer;
import java.util.TimerTask;

public class BankGrpcService extends BankserviceGrpc.BankserviceImplBase {

    @Override
    public void convert(Bank.ConvertcurrencyRequest request, StreamObserver<Bank.ConvertcurrencyResponse> responseObserver) {
        String currencyFrom=request.getCurrencyFrom();
        String currencyTo=request.getCurrencyTo();
        double amount=request.getAmount();

        Bank.ConvertcurrencyResponse response= Bank.ConvertcurrencyResponse.newBuilder()
                .setCurrencyFrom(currencyFrom)
                .setCurrencyTo(currencyTo)
                .setAmount(amount*11.64)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCurrentStream(Bank.ConvertcurrencyRequest request, StreamObserver<Bank.ConvertcurrencyResponse> responseObserver) {
        String currencyFrom=request.getCurrencyFrom();
        String currencyTo=request.getCurrencyTo();
        double amount=request.getAmount();

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            int contere=0;
            @Override
            public void run() {
                Bank.ConvertcurrencyResponse response= Bank.ConvertcurrencyResponse.newBuilder()
                        .setCurrencyFrom(currencyFrom)
                        .setCurrencyTo(currencyTo)
                        .setAmount(amount)
                        .setResult(amount*Math.random()*100)
                        .build();

                responseObserver.onNext(response);
                ++contere;
                if (contere==20){
                    responseObserver.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);}

    @Override
    public StreamObserver<Bank.ConvertcurrencyRequest> performStream(StreamObserver<Bank.ConvertcurrencyResponse> responseObserver) {
        return new StreamObserver<Bank.ConvertcurrencyRequest>() {
            double sum=0;
            @Override
            public void onNext(Bank.ConvertcurrencyRequest ConvertcurrencyRequest) {
                sum+=ConvertcurrencyRequest.getAmount();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                Bank.ConvertcurrencyResponse response= Bank.ConvertcurrencyResponse.newBuilder()
                        .setResult(sum*1.4)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();


            }
        };
    }

    @Override
    public StreamObserver<Bank.ConvertcurrencyRequest> fullCurrentStream(StreamObserver<Bank.ConvertcurrencyResponse> responseObserver) {
        return new StreamObserver<Bank.ConvertcurrencyRequest>() {
            @Override
            public void onNext(Bank.ConvertcurrencyRequest ConvertcurrencyRequest) {
                Bank.ConvertcurrencyResponse response= Bank.ConvertcurrencyResponse.newBuilder()
                        .setResult(ConvertcurrencyRequest.getAmount()*Math.random()*40)
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();

            }
        };
    }
}
