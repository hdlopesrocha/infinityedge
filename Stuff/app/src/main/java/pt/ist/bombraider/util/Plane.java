package pt.ist.bombraider.util;


public class Plane {
/*
    Vector3 Normal;
    float D;
    Plane()
    {
    }

    Plane(Vector3 normal, float d)
    {
        Normal = normal;
        D = d;
    }

    Plane(Vector3 a, Vector3 b, Vector3 c)
    {
        Vector3 ab = b - a;
        Vector3 ac = c - a;

        Vector3 cross = Vector3::Cross(ab, ac);
        Normal = Vector3::Normalize(cross);
        D = -(Vector3::Dot(cross, a));
    }

    Plane(float a, float b, float c, float d)
    {
        Normal = new  Vector3(a, b, c);
                D = d;
    }


    float DotCoordinate(Vector3 value)
    {
        return ((((Normal.getX() * value.getX()) + (Normal.getY() * value.getY())) + (Normal.getZ() * value.getZ())) + D);
    }

    float DotNormal(Vector3 value)
    {
        return (((Normal.getX() * value.getX()) + (Normal.getY() * value.getY())) + (Normal.getZ() * value.getZ()));
    }

    void Normalize()
    {
        float factor;
        Normal.normalize();
        factor = (float)Math.sqrt(Normal.getX() * Normal.getX() + Normal.getY() * Normal.getY() + Normal.getZ() * Normal.getZ()) /
                (float)Math.sqrt(Normal.getX() * Normal.getX() + Normal.getY() * Normal.getY() + Normal.getZ() * Normal.getZ());
        D = D * factor;
    }

*/


}
