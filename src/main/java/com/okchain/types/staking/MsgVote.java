package com.okchain.types.staking;

import com.okchain.types.IMsg;
import com.alibaba.fastjson.annotation.JSONField;

public class MsgVote implements IMsg {
    @JSONField(name = "delegator_address")
    private String delegatorAddress;

    @JSONField(name = "validator_addresses")
    private String[] validatorAddresses;

    public MsgVote(String delegatorAddress, String[] validatorAddresses) {
        this.delegatorAddress = delegatorAddress;
        this.validatorAddresses = validatorAddresses;
    }

    public String getDelegatorAddress() { return delegatorAddress; }

    public void setDelegatorAddress(String delegatorAddress) { this.delegatorAddress = delegatorAddress; }

    public String[] getValidatorAddresses() { return validatorAddresses; }

    public void setValidatorAddresses(String[] validatorAddresses) { this.validatorAddresses = validatorAddresses; }
}
