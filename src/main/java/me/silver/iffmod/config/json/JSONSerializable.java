package me.silver.iffmod.config.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;

public interface JSONSerializable extends Serializable {

    JSONObject serialize();

}
