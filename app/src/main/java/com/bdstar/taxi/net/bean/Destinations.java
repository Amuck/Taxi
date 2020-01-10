package com.bdstar.taxi.net.bean;

import java.util.List;

/**
 * {
 *         "code": 100,
 *         "message": "operation successful",
 *         "object": [
 *             {
 *                 "id": "1",
 *                 "destination": "成都东",
 *                 "creatTime": "2019-12-10 15:31:29"
 *             },
 *             {
 *                 "id": "2",
 *                 "destination": "火车北站",
 *                 "creatTime": "2019-12-10 15:31:29"
 *             },
 *             {
 *                 "id": "3",
 *                 "destination": "火车南站",
 *                 "creatTime": "2019-12-10 15:31:29"
 *             },
 *             {
 *                 "id": "4",
 *                 "destination": "成都西",
 *                 "creatTime": "2019-12-10 15:31:29"
 *             },
 *             {
 *                 "id": "5",
 *                 "destination": "天府广场",
 *                 "creatTime": "2019-12-10 15:31:29"
 *             },
 *             {
 *                 "id": "6",
 *                 "destination": "重庆",
 *                 "creatTime": "2019-12-10 15:31:29"
 *             }
 *         ]
 *     }
 */
public class Destinations {

    private int code;
    private String message;
    private List<Destination> object;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Destination> getObject() {
        return object;
    }

    public void setObject(List<Destination> object) {
        this.object = object;
    }

    public static class Destination{
        private String id;
        private String destination;
        private String creatTime;

        public Destination(){}

        public Destination(String destination){
            this.destination = destination;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
        }
    }
}
