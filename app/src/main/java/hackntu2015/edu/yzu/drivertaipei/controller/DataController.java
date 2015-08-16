package hackntu2015.edu.yzu.drivertaipei.controller;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hackntu2015.edu.yzu.drivertaipei.Node.NodeCarFlow;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeConstruct;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeGas;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeParkingLot;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeTraffic;
import hackntu2015.edu.yzu.drivertaipei.network.RequestClient;
import hackntu2015.edu.yzu.drivertaipei.utils.ErrorCode;

/**
 * Created by andy on 8/15/15.
 */
public class DataController {
    private String TAG = "DataController";

    private DataListener mListener = null;

    private List<NodeGas> nodeGas;
    private List<NodeParkingLot> nodeParkingLots;
    private List<NodeCarFlow> nodeCarFlows;
    private List<NodeTraffic> nodeTraffics;
    private List<NodeConstruct> nodeConstructs;

    DataController(){
        nodeGas = new ArrayList<NodeGas>();
        nodeParkingLots = new ArrayList<NodeParkingLot>();
        nodeCarFlows = new ArrayList<NodeCarFlow>();
        nodeTraffics = new ArrayList<NodeTraffic>();
        nodeConstructs = new ArrayList<NodeConstruct>();
    }

    void setListener(DataListener l){
        mListener = l;
    }

    void updateData(){
        RequestClient.request(new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                Log.e(TAG, "Data request fail: " + statusCode);
                //notifyFailure(ErrorCode.ERR_WRONGURL);
                mListener.onDataUpdate(null,getParkingLotData(),null,getConstructData(),getGasData());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, "Data request success: " + response.toString());
                try {
                    setGasData(response.getJSONArray("aa"));
                    setParkingLotData(response.getJSONArray("aa"));
                    setCarFlow(response.getJSONArray("aa"));
                    setConstructData(response.getJSONArray("aa"));
                    setTrafficsData(response.getJSONArray("aa"));
                    mListener.onDataUpdate(getCarFlowData(),getParkingLotData(),getTrafficData(),getConstructData(),getGasData());
                } catch (JSONException e) {
                    notifyFailure(ErrorCode.ERR_JSON);
                    e.printStackTrace();
                }
            }
        });
    }

    private void setGasData(JSONArray ja){
        for(int i = 0;i<ja.length();i++){
            try {
                NodeGas gas = new NodeGas(ja.getJSONObject(i));
                nodeGas.add(gas);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setParkingLotData(JSONArray ja){
        for(int i = 0;i<ja.length();i++){
            try {
                NodeParkingLot parkingLot = new NodeParkingLot(ja.getJSONObject(i));
                nodeParkingLots.add(parkingLot);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setTrafficsData(JSONArray ja){
        for(int i = 0;i<ja.length();i++){
            try {
                NodeTraffic traffic = new NodeTraffic(ja.getJSONObject(i));
                nodeTraffics.add(traffic);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setConstructData(JSONArray ja){
        for(int i = 0;i<ja.length();i++){
            try {
                NodeConstruct construct = new NodeConstruct(ja.getJSONObject(i));
                nodeConstructs.add(construct);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCarFlow(JSONArray ja){
        for(int i = 0;i<ja.length();i++){
            try {
                NodeCarFlow carFlow = new NodeCarFlow(ja.getJSONObject(i));
                nodeCarFlows.add(carFlow);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public List<NodeGas> getGasData(){
        nodeGas.add(new NodeGas());
        return nodeGas;
    }
    public List<NodeCarFlow> getCarFlowData(){
        return nodeCarFlows;
    }

    public List<NodeTraffic> getTrafficData(){
        return nodeTraffics;
    }

    public List<NodeConstruct> getConstructData(){
        nodeConstructs.add(new NodeConstruct());
        return nodeConstructs;
    }

    public List<NodeParkingLot> getParkingLotData(){
        nodeParkingLots.add(new NodeParkingLot());
        return nodeParkingLots;
    }

    public void notifyFailure(ErrorCode err){
        if(mListener != null){
            mListener.onDataConnectFailed(err);
        }
    }
}
