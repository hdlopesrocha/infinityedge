package pt.ist.bombraider.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import pt.ist.bombraider.util.Camera;
import pt.ist.bombraider.util.FileString;
import pt.ist.bombraider.util.Material;
import pt.ist.bombraider.util.ShaderProgram;
import pt.ist.bombraider.util.TextureLoader;

public class Model3D extends Model {

    public ArrayList<Group> groups = new ArrayList<Group>();


    public Model3D(Context context, int resource, float scale) {
        TreeMap<String, Material> materials = new TreeMap<String, Material>();
        try {
            JSONObject jObject = new JSONObject(FileString.read(context, resource));
            {
                JSONObject jMats = jObject.getJSONObject("materials");

                Iterator<String> iterator = jMats.keys();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    JSONObject jMat = jMats.getJSONObject(key);

                    Material currentMaterial = new Material();
                    materials.put(key, currentMaterial);

                    if (jMat.has("map_Kd")) {
                        currentMaterial.setTexture(context, jMat.getString("map_Kd"));
                    }
                    if (jMat.has("Ka")) {
                        JSONArray array = jMat.getJSONArray("Ka");
                        currentMaterial.Ka = new Float[3];
                        for (int j = 0; j < 3; ++j)
                            currentMaterial.Ka[j] = (float) array.getDouble(j);
                    }
                    if (jMat.has("Kd")) {
                        JSONArray array = jMat.getJSONArray("Kd");
                        currentMaterial.Kd = new Float[3];
                        for (int j = 0; j < 3; ++j)
                            currentMaterial.Kd[j] = (float) array.getDouble(j);
                    }
                    if (jMat.has("Ks")) {
                        JSONArray array = jMat.getJSONArray("Ks");
                        currentMaterial.Ks = new Float[3];
                        for (int j = 0; j < 3; ++j)
                            currentMaterial.Ks[j] = (float) array.getDouble(j);
                    }
                    if (jMat.has("Tf")) {
                        currentMaterial.Tf = (float) jMat.getDouble("Tf");
                    }
                    if (jMat.has("illum")) {
                        currentMaterial.illum = (float) jMat.getDouble("illum");
                    }
                    if (jMat.has("d")) {
                        currentMaterial.d = (float) jMat.getDouble("d");
                    }
                    if (jMat.has("Ns")) {
                        currentMaterial.Ns = (float) jMat.getDouble("Ns");
                    }
                    if (jMat.has("sharpness")) {
                        currentMaterial.sharpness = (float) jMat.getDouble("sharpness");
                    }
                    if (jMat.has("Ni")) {
                        currentMaterial.Ni = (float) jMat.getDouble("Ni");
                    }

                }
            }
            {
                JSONObject jGroups = jObject.getJSONObject("groups");


                Iterator<String> iterator = jGroups.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();

                    Group currentGroup = new Group();
                    groups.add(currentGroup);
                    JSONArray subGroups = jGroups.getJSONArray(key);
                    currentGroup.name = key;
                    for (int j = 0; j < subGroups.length(); ++j) {
                        JSONObject jSubGroup = subGroups.getJSONObject(j);
                        BufferObject currentSubGroup = new BufferObject();
                        currentGroup.subGroups.add(currentSubGroup);

                        if (jSubGroup.has("mm")) {
                            currentSubGroup.setMaterial(materials.get(jSubGroup.getString("mm")));
                        }
                        JSONArray vv = jSubGroup.getJSONArray("vv");
                        for (int k = 0; k < vv.length(); ++k) {
                            float val = (float) vv.getDouble(k) * scale;
                            if (k % 3 == 1) {
                                currentGroup.maxY = Math.max(currentGroup.maxY, val);
                            }
                            currentSubGroup.addVertex(val);
                        }
                        JSONArray vn = jSubGroup.getJSONArray("vn");
                        for (int k = 0; k < vn.length(); ++k) {
                            currentSubGroup.addNormal((float) vn.getDouble(k));
                        }
                        JSONArray vt = jSubGroup.getJSONArray("vt");
                        for (int k = 0; k < vt.length(); ++k) {
                            currentSubGroup.addTexture((float) vt.getDouble(k));
                        }
                        JSONArray ii = jSubGroup.getJSONArray("ii");
                        for (int k = 0; k < ii.length(); ++k) {
                            currentSubGroup.addIndex((short) ii.getInt(k));
                        }

                        currentSubGroup.buildBuffer();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(ShaderProgram shader,Camera camera) {
        for (Group g : groups) {
            for (BufferObject sg : g.subGroups) {
                sg.draw(shader,camera);
            }
        }
    }
}
