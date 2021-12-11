package com.uyu.device.devicetraining.domain.motor;

import com.uyu.device.devicetraining.data.entity.config.ApproachConfig;
import com.uyu.device.devicetraining.data.entity.config.FracturedRulerConfig;
import com.uyu.device.devicetraining.data.entity.trainpres.ApproachTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.FollowTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.FracturedRulerTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.GlanceTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGFixedVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGVariableVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RedGreenReadTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.StereoscopeTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.motor.DirectionType;
import com.uyu.device.devicetraining.data.motor.DistanceHandler;
import com.uyu.device.devicetraining.data.motor.TurntableLocation;

/**
 * Created by windern on 2016/1/25.
 */
public class TrainUseCase {
    /**
     * 创建命令集
     * -1代表不移动
     * @param screenLocation
     * @param baffleLocation
     * @param leftLocation
     * @param rightLocation
     * @return
     */
    private static ControlMessageSet create(int screenLocation,int baffleLocation,int leftLocation,int rightLocation){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();

        if(screenLocation!=-1) {
            ScreenMotorController screenMotCtrl = motorBus.screenMotCtrl;
            ControlMessage screenMessage = screenMotCtrl.setLocation(screenLocation);
            //调整到0的都采用复位命令
            if(screenLocation==0){
                screenMessage = screenMotCtrl.reset();
            }
            messageSet.addMessage(screenMessage);
        }

        if(baffleLocation!=-1) {
            BaffleMotorController baffleMotCtrl = motorBus.baffleMotCtrl;
            ControlMessage baffleMessage = baffleMotCtrl.setLocation(baffleLocation);
            //调整到0的都采用复位命令
            if(baffleLocation==0){
                baffleMessage = baffleMotCtrl.reset();
            }
            messageSet.addMessage(baffleMessage);
        }

        if(leftLocation!=-1) {
            TurntableMotorController leftTurntableMotCtrl = motorBus.leftTurntableMotCtrl;
            ControlMessage leftMessage = leftTurntableMotCtrl.setLocation(leftLocation);
            //调整到0的都采用复位命令
            if(leftLocation==0){
                leftMessage = leftTurntableMotCtrl.reset();
            }
            messageSet.addMessage(leftMessage);
        }

        if(rightLocation!=-1) {
            TurntableMotorController rightTurntableMotCtrl = motorBus.rightTurntableMotCtrl;
            ControlMessage rightMessage = rightTurntableMotCtrl.setLocation(rightLocation);
            //调整到0的都采用复位命令
            if(rightLocation==0){
                rightMessage = rightTurntableMotCtrl.reset();
            }
            messageSet.addMessage(rightMessage);
        }

        return messageSet;
    }

    public static ControlMessageSet create(TrainPres trainPres){
        ControlMessageSet messageSet = null;
        int screenLocation400 = DistanceHandler.computeMotorScreenDistance(400);
        if(trainPres instanceof StereoscopeTrainPres){
            int motorScreenLocation = DistanceHandler.computeMotorScreenDistance(((StereoscopeTrainPres) trainPres).getScreenLocation());
            messageSet = create(motorScreenLocation,0, TurntableLocation.PRISM.getValue(),TurntableLocation.PRISM.getValue());
        }else if(trainPres instanceof FracturedRulerTrainPres){
            //初始的时候根据初始关数决定
            int level = ((FracturedRulerTrainPres) trainPres).getStartDifficulty()-1;
            if(level<0){
                level = 0;
            }
            int baffleLocation = FracturedRulerConfig.getBaffleLocation(level);
            int motorBaffleLocation = DistanceHandler.computeMotorBaffleDistanceInFracturedRuler(baffleLocation);
            messageSet = create(screenLocation400, motorBaffleLocation, TurntableLocation.ZERO_DIOPTER.getValue(), TurntableLocation.ZERO_DIOPTER.getValue());
        }else if((trainPres instanceof RedGreenReadTrainPres)
                || (trainPres instanceof RGVariableVectorTrainPres)
                || (trainPres instanceof RGFixedVectorTrainPres)){
            messageSet = create(screenLocation400, 0, TurntableLocation.RED_GREEN.getValue(), TurntableLocation.RED_GREEN.getValue());
        }else if(trainPres instanceof ApproachTrainPres){
            int baffleLocation = DistanceHandler.computeBaffleVirtualDistanceInApproach(ApproachConfig.MaxDistance);
            messageSet = create(0, baffleLocation, TurntableLocation.ZERO_DIOPTER.getValue(), TurntableLocation.ZERO_DIOPTER.getValue());
        }else if((trainPres instanceof GlanceTrainPres)
                || (trainPres instanceof FollowTrainPres)){
            messageSet = create(screenLocation400, 0, TurntableLocation.ZERO_DIOPTER.getValue(), TurntableLocation.ZERO_DIOPTER.getValue());
        }else if(trainPres instanceof ReversalTrainPres){
            if(((ReversalTrainPres) trainPres).getEyeType()== EnumEyeType.LEFT){
                TurntableLocation leftTurntableLocation = TurntableLocation.getZhengByDegreeLevel(((ReversalTrainPres) trainPres).getLPositiveDegreeLevel());
                messageSet = create(screenLocation400, 0, leftTurntableLocation.getValue(), TurntableLocation.BLANK.getValue());
            }else if(((ReversalTrainPres) trainPres).getEyeType()== EnumEyeType.RIGHT){
                TurntableLocation rightTurntableLocation = TurntableLocation.getZhengByDegreeLevel(((ReversalTrainPres) trainPres).getRPositiveDegreeLevel());
                messageSet = create(screenLocation400, 0,TurntableLocation.BLANK.getValue(), rightTurntableLocation.getValue());
            }else if(((ReversalTrainPres) trainPres).getEyeType()== EnumEyeType.DOUBLE){
                TurntableLocation leftTurntableLocation = TurntableLocation.getZhengByDegreeLevel(((ReversalTrainPres) trainPres).getLPositiveDegreeLevel());
                TurntableLocation rightTurntableLocation = TurntableLocation.getZhengByDegreeLevel(((ReversalTrainPres) trainPres).getRPositiveDegreeLevel());
                messageSet = create(screenLocation400, 0, leftTurntableLocation.getValue(), rightTurntableLocation.getValue());
            }
        }
        return messageSet;
    }

    public static ControlMessageSet createFracturedRulerPrepare(int level){
        int baffleLocation = FracturedRulerConfig.getBaffleLocation(level);
        int motorBaffleLocation = DistanceHandler.computeMotorBaffleDistanceInFracturedRuler(baffleLocation);
        ControlMessageSet messageSet = create(-1, motorBaffleLocation, -1, -1);
        return messageSet;
    }

    public static ControlMessageSet createReversalPrepare(int leftLoc,int rightLoc){
        ControlMessageSet messageSet = create(-1,-1,leftLoc,rightLoc);
        return messageSet;
    }

    public static ControlMessageSet createBaffleMove(DirectionType directionType,int speedLevel){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();
        BaffleMotorController baffleMotCtrl = motorBus.baffleMotCtrl;
        ControlMessage baffleMessage = baffleMotCtrl.move(directionType.getValue(), speedLevel);
        messageSet.addMessage(baffleMessage);
        return messageSet;
    }

    public static ControlMessageSet createBaffleStop(){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();
        BaffleMotorController baffleMotCtrl = motorBus.baffleMotCtrl;
        ControlMessage baffleMessage = baffleMotCtrl.stop();
        messageSet.addMessage(baffleMessage);
        return messageSet;
    }

    public static ControlMessageSet createBaffleGet(){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();
        BaffleMotorController baffleMotCtrl = motorBus.baffleMotCtrl;
        ControlMessage baffleMessage = baffleMotCtrl.getLocation();
        messageSet.addMessage(baffleMessage);
        return messageSet;
    }

    /**
     * 全部重置
     * @return
     */
    public static ControlMessageSet reset(){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();

        ScreenMotorController screenMotCtrl = motorBus.screenMotCtrl;
        ControlMessage screenMessage = screenMotCtrl.reset();
        messageSet.addMessage(screenMessage);

        BaffleMotorController baffleMotCtrl = motorBus.baffleMotCtrl;
        ControlMessage baffleMessage = baffleMotCtrl.reset();
        messageSet.addMessage(baffleMessage);

//        PupilMotorController pupilMotCtrl = motorBus.pupilMotCtrl;
//        ControlMessage pupilMessage = pupilMotCtrl.reset();
//        messageSet.addMessage(pupilMessage);

        TurntableMotorController leftTurntableMotCtrl = motorBus.leftTurntableMotCtrl;
        ControlMessage leftMessage = leftTurntableMotCtrl.reset();
        messageSet.addMessage(leftMessage);

        TurntableMotorController rightTurntableMotCtrl = motorBus.rightTurntableMotCtrl;
        ControlMessage rightMessage = rightTurntableMotCtrl.reset();
        messageSet.addMessage(rightMessage);

        return messageSet;
    }

    /**
     * 获取按钮是否按下
     * @return
     */
    public static ControlMessageSet getButtonIsPress(){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();

        ButtonMotorController buttonMotCtrl = motorBus.buttonMotCtrl;
        ControlMessage buttonMessage = buttonMotCtrl.getButtonIsPress();
        messageSet.addMessage(buttonMessage);

        return messageSet;
    }

    /**
     * 获取按钮是否按下
     * @return
     */
    public static ControlMessageSet adjustPupileDistance(int pupilDistance){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();

        PupilMotorController pupilMotCtrl = motorBus.pupilMotCtrl;
        ControlMessage pupilMessage = pupilMotCtrl.setPupillaryDistance(pupilDistance);
        messageSet.addMessage(pupilMessage);

        return messageSet;
    }

    public static ControlMessageSet resetGlass(){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();

        TurntableMotorController leftTurntableMotCtrl = motorBus.leftTurntableMotCtrl;
        ControlMessage leftMessage = leftTurntableMotCtrl.reset();
        messageSet.addMessage(leftMessage);

        TurntableMotorController rightTurntableMotCtrl = motorBus.rightTurntableMotCtrl;
        ControlMessage rightMessage = rightTurntableMotCtrl.reset();
        messageSet.addMessage(rightMessage);

        return messageSet;
    }

    public static ControlMessageSet resetGlass(int status){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();

        if(status == 0) {
            TurntableMotorController leftTurntableMotCtrl = motorBus.leftTurntableMotCtrl;
            ControlMessage leftMessage = leftTurntableMotCtrl.reset();
            messageSet.addMessage(leftMessage);
        }else if(status == 1){
            TurntableMotorController rightTurntableMotCtrl = motorBus.rightTurntableMotCtrl;
            ControlMessage rightMessage = rightTurntableMotCtrl.reset();
            messageSet.addMessage(rightMessage);
        }else if(status == 2){
            TurntableMotorController leftTurntableMotCtrl = motorBus.leftTurntableMotCtrl;
            ControlMessage leftMessage = leftTurntableMotCtrl.reset();
            messageSet.addMessage(leftMessage);

            TurntableMotorController rightTurntableMotCtrl = motorBus.rightTurntableMotCtrl;
            ControlMessage rightMessage = rightTurntableMotCtrl.reset();
            messageSet.addMessage(rightMessage);
        }

        return messageSet;
    }

    /**
     * 变换充电状态
     * @param chargeStatus
     * @return
     */
    public static ControlMessageSet changeChargeStatus(int chargeStatus){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();

        ChargeMotorController chargeMotCtrl = motorBus.chargeMotCtrl;
        ControlMessage chargeMessage = chargeMotCtrl.changeChargeStatus(chargeStatus);
        messageSet.addMessage(chargeMessage);

        return messageSet;
    }
}
