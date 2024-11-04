package org.davex.chainmaker.config;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import org.chainmaker.pb.common.ChainmakerBlock;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.ChainClientException;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;


public class Subscribe extends InitClient implements Runnable{
    @Override
    public void run() {

    }

    private static final boolean ONLY_HEADER =false;
    static public void testSubscribeBlock(){
        StreamObserver<ResultOuterClass.SubscribeResult> responseObserver = new StreamObserver<ResultOuterClass.SubscribeResult>() {
            @Override
            public void onNext(ResultOuterClass.SubscribeResult result) {
                try{
                    if(ONLY_HEADER){
                        ChainmakerBlock.BlockHeader blockHeader = ChainmakerBlock.BlockHeader.parseFrom(result.getData());
                        System.out.println("###new block header:");
                        System.out.println(" height:" + blockHeader.getBlockHeight());
                        System.out.println(" tx-count:" + blockHeader.getTxCount());
                    }else {
                        ChainmakerBlock.BlockInfo blockinfo = ChainmakerBlock.BlockInfo.parseFrom(result.getData());
                        System.out.println("###new block:");
                        System.out.println( " height:" + blockinfo.getBlock().getHeader().getBlockHeight());
                        System.out.println(" tx-count:"+blockinfo.getBlock().getTxsCount());
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                // just do nothing
            }
        };

        StreamObserver<ResultOuterClass.SubscribeResult> responseObserverTx = new StreamObserver<ResultOuterClass.SubscribeResult>() {
            @Override
            public void onNext(ResultOuterClass.SubscribeResult result) {
                try {
                    ChainmakerTransaction.Transaction transactionInfo = ChainmakerTransaction.Transaction.parseFrom(result.getData());
                    System.out.print("订阅到： txId:" + transactionInfo.getPayload().getTxId());
                    System.out.print(", code:" + transactionInfo.getResult().getCode().getNumber());
                    if (transactionInfo.getResult().getCode().getNumber() == ResultOuterClass.TxStatusCode.SUCCESS.getNumber()) {
                        System.out.println(", result :" + transactionInfo.getResult().getContractResult().getResult());
                    } else {
                        System.out.print(", message:" + transactionInfo.getResult().getMessage());
                        System.out.println(", contract message:" + transactionInfo.getResult().getContractResult().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                // can add log here
            }

            @Override
            public void onCompleted() {
                // can add log here
            }
        };

        try{
            chainClient.subscribeBlock(0, 1000, true, ONLY_HEADER, responseObserver);
            System.out.println("开始订阅区块");
            Thread.sleep(1000 * 2);

            chainClient.subscribeTx(0, 1000, "", new String[]{}, responseObserverTx);
            System.out.println("开始订阅交易");
            Thread.sleep(1000 * 10);
        }  catch (Exception e){
            e.printStackTrace();
        }

    }
}
