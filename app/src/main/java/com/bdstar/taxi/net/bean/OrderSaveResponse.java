package com.bdstar.taxi.net.bean;

/**
 * {
 *     "success": true,
 *     "error": null,
 *     "data": {
 *         "code": 100,   ------------------>只要是100的都是成功的
 *         "message": "operation successful",
 *         "object": {
 *             "isok": 1,
 *             "id": "f8952b3487b14fa08f3b65f79f6e4f1b20191212105710"  -------->生成的运单id 修改评奖是索要id
 *         }
 *     }
 * }
 */
public class OrderSaveResponse {
    private boolean success;
    private String error;
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private int code;
        private String message;
        private Obj object;

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

        public Obj getObject() {
            return object;
        }

        public void setObject(Obj object) {
            this.object = object;
        }
    }

    public static class Obj{
        private int isok;
        private String id;

        public int getIsok() {
            return isok;
        }

        public void setIsok(int isok) {
            this.isok = isok;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
