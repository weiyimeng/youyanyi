package com.uyu.device.devicetraining.domain.motor;

import com.uyu.device.devicetraining.data.entity.config.TrainConfig;
import com.uyu.device.devicetraining.data.motor.BackMessageSet;
import com.uyu.device.devicetraining.data.motor.ButtonMotor;
import com.uyu.device.devicetraining.data.motor.ChargeMotor;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.motor.FlagType;
import com.uyu.device.devicetraining.data.motor.GapMotor;
import com.uyu.device.devicetraining.data.motor.LineMotor;
import com.uyu.device.devicetraining.data.motor.BackMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.TurntableMotor;
import com.uyu.device.devicetraining.domain.bluetooth.ByteCommand;
import com.uyu.device.devicetraining.domain.bluetooth.ComConfig;
import com.uyu.device.devicetraining.domain.bluetooth.ComFrame;
import com.uyu.device.devicetraining.domain.bluetooth.CommandConvertException;
import com.uyu.device.devicetraining.domain.bluetooth.tradition.MotorBluetoothAdapter;
import com.uyu.device.devicetraining.presentation.view.adapter.OnButtonPressListener;
import com.uyu.device.devicetraining.presentation.view.adapter.OnChangeChargeListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by windern on 2016/1/18.
 */
public class MotorBus {
    public ScreenMotorController screenMotCtrl;
    public BaffleMotorController baffleMotCtrl;
    public PupilMotorController pupilMotCtrl;
    public TurntableMotorController leftTurntableMotCtrl;
    public TurntableMotorController rightTurntableMotCtrl;
    public ButtonMotorController buttonMotCtrl;
    public ChargeMotorController chargeMotCtrl;

    private SocketService socketService;

    /**
     * ????????????????????????????????????
     */
    private ArrayList<ControlMessage> listMessages = new ArrayList<>();
    /**
     * ???????????????
     */
    private LinkedList<ControlMessageSet> queue = new LinkedList<>();
    private ControlMessageSet nowMessageSet = null;

    private OnButtonPressListener onButtonPressListener;
    private OnChangeChargeListener onChangeChargeListener;

    public OnButtonPressListener getOnButtonPressListener() {
        return onButtonPressListener;
    }

    public void setOnButtonPressListener(OnButtonPressListener onButtonPressListener) {
        this.onButtonPressListener = onButtonPressListener;
    }

    public OnChangeChargeListener getOnChangeChargeListener() {
        return onChangeChargeListener;
    }

    public void setOnChangeChargeListener(OnChangeChargeListener onChangeChargeListener) {
        this.onChangeChargeListener = onChangeChargeListener;
    }

    private static MotorBus instatnce = null;

    public static MotorBus getInstance() {
        if (instatnce == null) {
            instatnce = new MotorBus();
        }
        return instatnce;
    }

    /**
     * ??????????????????????????????????????????
     */
    private MotorBus() {
        screenMotCtrl = new ScreenMotorController();
        LineMotor screenMotor = new LineMotor();
        screenMotor.setNum(1);
        screenMotCtrl.setMotor(screenMotor);
        screenMotCtrl.setMotorBus(this);

        baffleMotCtrl = new BaffleMotorController();
        LineMotor baffleMotor = new LineMotor();
        baffleMotor.setNum(2);
        baffleMotCtrl.setMotor(baffleMotor);
        baffleMotCtrl.setMotorBus(this);

        pupilMotCtrl = new PupilMotorController();
        GapMotor pupilMotor = new GapMotor();
        pupilMotor.setNum(3);
        pupilMotCtrl.setMotor(pupilMotor);
        pupilMotCtrl.setMotorBus(this);

        leftTurntableMotCtrl = new TurntableMotorController();
        TurntableMotor leftTurntableMotor = new TurntableMotor();
        leftTurntableMotor.setNum(5);
        leftTurntableMotCtrl.setMotor(leftTurntableMotor);
        leftTurntableMotCtrl.setMotorBus(this);

        rightTurntableMotCtrl = new TurntableMotorController();
        TurntableMotor rightTurntableMotor = new TurntableMotor();
        rightTurntableMotor.setNum(4);
        rightTurntableMotCtrl.setMotor(rightTurntableMotor);
        rightTurntableMotCtrl.setMotorBus(this);

        //?????????
        buttonMotCtrl = new ButtonMotorController();
        ButtonMotor buttonMotor = new ButtonMotor();
        buttonMotor.setNum(6);
        buttonMotCtrl.setMotor(buttonMotor);
        buttonMotCtrl.setMotorBus(this);

        //????????????
        chargeMotCtrl = new ChargeMotorController();
        ChargeMotor chargeMotor = new ChargeMotor();
        chargeMotor.setNum(7);
        chargeMotCtrl.setMotor(chargeMotor);
        chargeMotCtrl.setMotorBus(this);

        //?????????????????????????????????500??????????????????
        startLoopTimer();
    }

    public void setSocketService(SocketService socketService) {
        this.socketService = socketService;
    }

    private Subscription loopTimer;

    public void startLoopTimer() {
        loopTimer = Observable.interval(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> {
                    handleNowMessageSet();
                });
    }

    public void stopLoopTimer() {
        if (loopTimer != null && !loopTimer.isUnsubscribed()) {
            loopTimer.unsubscribe();
            loopTimer = null;
        }
    }

    /**
     * ??????????????????
     *
     * @param controlMessageSet
     * @return
     */
    public PublishSubject<ControlMessageSet> sendMessageSetDirect(ControlMessageSet controlMessageSet) {
//        Timber.d("sendMessageSet:%s",controlMessageSet.toString());
        PublishSubject<ControlMessageSet> publishSubject = PublishSubject.create();
        controlMessageSet.setPublishSubject(publishSubject);

        ComFrame comFrame = ComFrame.getInstance();
        try {
            if (controlMessageSet != null) {
                ArrayList<ControlMessage> notReadyMessageList = controlMessageSet.getNotReadyMessageList();
                byte[] bytes = comFrame.computeSendByte(notReadyMessageList);
                String bytesString = ComConfig.bytesToHexString(bytes);
                Timber.d("handleNowMessageSet:string:%s", notReadyMessageList.toString());
                Timber.d("handleNowMessageSet:bytes:%s", bytesString);

                MotorBluetoothAdapter.getInstance().sendFromMotorBus(bytes);
            }
        } catch (CommandConvertException e) {
            e.printStackTrace();
        }

        return publishSubject;
    }

    /**
     * ?????????????????????
     *
     * @param controlMessageSet
     * @return
     */
    public PublishSubject<ControlMessageSet> sendMessageSet(ControlMessageSet controlMessageSet) {
//        Timber.d("sendMessageSet:%s",controlMessageSet.toString());
        PublishSubject<ControlMessageSet> publishSubject = PublishSubject.create();
        controlMessageSet.setPublishSubject(publishSubject);

        queue.offer(controlMessageSet);
        handleNextMessageSet();

        return publishSubject;
    }

    public void handleNextMessageSet() {
        //???????????????????????????
        if (nowMessageSet == null) {
            if (queue.size() > 0) {
                nowMessageSet = queue.poll();

                //???????????????
                for (int i = 0; i < nowMessageSet.getList().size(); i++) {
                    ControlMessage controlMessage = nowMessageSet.getList().get(i);
                    controlMessage.setSeqNum(listMessages.size() + 1);
                    listMessages.add(controlMessage);
                }

                handleNowMessageSet();
            }
        }
    }

    public void handleNowMessageSet() {
        ComFrame comFrame = ComFrame.getInstance();
        try {
            if (nowMessageSet != null) {
                ArrayList<ControlMessage> notReadyMessageList = nowMessageSet.getNotReadyMessageList();
                byte[] bytes = comFrame.computeSendByte(notReadyMessageList);
                String bytesString = ComConfig.bytesToHexString(bytes);
                Timber.d("handleNowMessageSet:string:%s", notReadyMessageList.toString());
                Timber.d("handleNowMessageSet:bytes:%s", bytesString);

                MotorBluetoothAdapter.getInstance().sendFromMotorBus(bytes);
            } else {
                //?????????????????????nowMessageSet???????????????????????????????????????
                handleNextMessageSet();
            }
        } catch (CommandConvertException e) {
            e.printStackTrace();
        }
    }

    public void receiveButtonCommand() {
        Timber.d("windern:receiveButtonCommand");
        if (onButtonPressListener != null) {
            Observable.just(1).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        onButtonPressListener.onButtonPress();
                    });
        }
    }

    public void receiveChangeChargeCommand(int chargeStatus) {
        Timber.d("windern:receiveChangeChargeCommand");
        if (onButtonPressListener != null) {
            Observable.just(1).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        onChangeChargeListener.onChangeCharge(chargeStatus);
                    });
        }
    }


    public void receiveBackByteCommand(ByteCommand[] arrayByteCommand) {
        if (arrayByteCommand != null) {
            BackMessageSet backMessageSet = new BackMessageSet();
            for (int i = 0; i < arrayByteCommand.length; i++) {
                ByteCommand byteCommand = arrayByteCommand[i];
                BackMessage backMessage = byteCommand.createBackMessage(nowMessageSet);
                if (backMessage != null) {
                    if (backMessage.isButtonPressMessage()) {
                        //???????????????seq_num=0,??????1???????????????????????????
                        if (backMessage.getFlag() == FlagType.SUCCESS && backMessage.getValue() == 1) {
                            receiveButtonCommand();
                        }
                    } else if (backMessage.isChangeChargeMessage()) {
                        //???????????????seq_num=-1???????????????????????????????????????
                        if (backMessage.getFlag() == FlagType.SUCCESS) {
                            receiveChangeChargeCommand(backMessage.getValue());
                        }
                    } else {
                        backMessageSet.getList().add(backMessage);
                    }
                }
            }
            Timber.d("windern:receiveBackByteCommand:backMessageSet:%s", backMessageSet.toString());
            receiveMotorMsg(backMessageSet);
        } else {
            //????????????????????????null??????????????????????????????????????????????????????????????????????????????
            receiveMotorMsg(null);
        }
    }

    public void receiveMotorMsg(BackMessageSet backMessageSet) {
        if (backMessageSet != null && backMessageSet.getList().size() > 0) {
            if (nowMessageSet != null) {
                nowMessageSet.handleBackMessageSet(backMessageSet);
                if (nowMessageSet.isReady()) {
                    //????????????????????????????????????????????????
                    //??????????????????
                    //Handler handler = nowMessageSet.getHandler();
                    PublishSubject<ControlMessageSet> publishSubject = nowMessageSet.getPublishSubject();
                    if (publishSubject != null) {
                        publishSubject.onNext(nowMessageSet);
                        //nowMessageSet.getHandler().sendMessage(nowMessageSet.toMessage());
                    }
                    nowMessageSet = null;
                    handleNextMessageSet();
                } else {
                    handleNowMessageSet();
                }
            }
        } else {
            handleNowMessageSet();
        }
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public boolean removeAllMessage() {
        List<ControlMessageSet> tmpList = new ArrayList<>();
        return queue.removeAll(tmpList);
    }


    /**
     * ??????
     */
    public void reset() {
        Timber.d("????????????????????????");
        ControlMessageSet messageSet = TrainUseCase.reset();
        MotorBus.getInstance().sendMessageSet(messageSet);
    }

    /**
     * ????????????
     */
    public void adjustPupilDistance() {
        ControlMessageSet messageSet = TrainUseCase.adjustPupileDistance(TrainConfig.PupilDefaultDistance);
        MotorBus.getInstance().sendMessageSet(messageSet);
    }
}
