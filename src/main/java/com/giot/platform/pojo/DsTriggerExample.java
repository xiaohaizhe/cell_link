package com.giot.platform.pojo;

import java.util.ArrayList;
import java.util.List;

public class DsTriggerExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DsTriggerExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andDsidIsNull() {
            addCriterion("dsId is null");
            return (Criteria) this;
        }

        public Criteria andDsidIsNotNull() {
            addCriterion("dsId is not null");
            return (Criteria) this;
        }

        public Criteria andDsidEqualTo(Integer value) {
            addCriterion("dsId =", value, "dsid");
            return (Criteria) this;
        }

        public Criteria andDsidNotEqualTo(Integer value) {
            addCriterion("dsId <>", value, "dsid");
            return (Criteria) this;
        }

        public Criteria andDsidGreaterThan(Integer value) {
            addCriterion("dsId >", value, "dsid");
            return (Criteria) this;
        }

        public Criteria andDsidGreaterThanOrEqualTo(Integer value) {
            addCriterion("dsId >=", value, "dsid");
            return (Criteria) this;
        }

        public Criteria andDsidLessThan(Integer value) {
            addCriterion("dsId <", value, "dsid");
            return (Criteria) this;
        }

        public Criteria andDsidLessThanOrEqualTo(Integer value) {
            addCriterion("dsId <=", value, "dsid");
            return (Criteria) this;
        }

        public Criteria andDsidIn(List<Integer> values) {
            addCriterion("dsId in", values, "dsid");
            return (Criteria) this;
        }

        public Criteria andDsidNotIn(List<Integer> values) {
            addCriterion("dsId not in", values, "dsid");
            return (Criteria) this;
        }

        public Criteria andDsidBetween(Integer value1, Integer value2) {
            addCriterion("dsId between", value1, value2, "dsid");
            return (Criteria) this;
        }

        public Criteria andDsidNotBetween(Integer value1, Integer value2) {
            addCriterion("dsId not between", value1, value2, "dsid");
            return (Criteria) this;
        }

        public Criteria andTriggeridIsNull() {
            addCriterion("triggerId is null");
            return (Criteria) this;
        }

        public Criteria andTriggeridIsNotNull() {
            addCriterion("triggerId is not null");
            return (Criteria) this;
        }

        public Criteria andTriggeridEqualTo(Integer value) {
            addCriterion("triggerId =", value, "triggerid");
            return (Criteria) this;
        }

        public Criteria andTriggeridNotEqualTo(Integer value) {
            addCriterion("triggerId <>", value, "triggerid");
            return (Criteria) this;
        }

        public Criteria andTriggeridGreaterThan(Integer value) {
            addCriterion("triggerId >", value, "triggerid");
            return (Criteria) this;
        }

        public Criteria andTriggeridGreaterThanOrEqualTo(Integer value) {
            addCriterion("triggerId >=", value, "triggerid");
            return (Criteria) this;
        }

        public Criteria andTriggeridLessThan(Integer value) {
            addCriterion("triggerId <", value, "triggerid");
            return (Criteria) this;
        }

        public Criteria andTriggeridLessThanOrEqualTo(Integer value) {
            addCriterion("triggerId <=", value, "triggerid");
            return (Criteria) this;
        }

        public Criteria andTriggeridIn(List<Integer> values) {
            addCriterion("triggerId in", values, "triggerid");
            return (Criteria) this;
        }

        public Criteria andTriggeridNotIn(List<Integer> values) {
            addCriterion("triggerId not in", values, "triggerid");
            return (Criteria) this;
        }

        public Criteria andTriggeridBetween(Integer value1, Integer value2) {
            addCriterion("triggerId between", value1, value2, "triggerid");
            return (Criteria) this;
        }

        public Criteria andTriggeridNotBetween(Integer value1, Integer value2) {
            addCriterion("triggerId not between", value1, value2, "triggerid");
            return (Criteria) this;
        }

        public Criteria andDsNameIsNull() {
            addCriterion("ds_name is null");
            return (Criteria) this;
        }

        public Criteria andDsNameIsNotNull() {
            addCriterion("ds_name is not null");
            return (Criteria) this;
        }

        public Criteria andDsNameEqualTo(String value) {
            addCriterion("ds_name =", value, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameNotEqualTo(String value) {
            addCriterion("ds_name <>", value, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameGreaterThan(String value) {
            addCriterion("ds_name >", value, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameGreaterThanOrEqualTo(String value) {
            addCriterion("ds_name >=", value, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameLessThan(String value) {
            addCriterion("ds_name <", value, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameLessThanOrEqualTo(String value) {
            addCriterion("ds_name <=", value, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameLike(String value) {
            addCriterion("ds_name like", value, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameNotLike(String value) {
            addCriterion("ds_name not like", value, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameIn(List<String> values) {
            addCriterion("ds_name in", values, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameNotIn(List<String> values) {
            addCriterion("ds_name not in", values, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameBetween(String value1, String value2) {
            addCriterion("ds_name between", value1, value2, "dsName");
            return (Criteria) this;
        }

        public Criteria andDsNameNotBetween(String value1, String value2) {
            addCriterion("ds_name not between", value1, value2, "dsName");
            return (Criteria) this;
        }

        public Criteria andModeIsNull() {
            addCriterion("`mode` is null");
            return (Criteria) this;
        }

        public Criteria andModeIsNotNull() {
            addCriterion("`mode` is not null");
            return (Criteria) this;
        }

        public Criteria andModeEqualTo(Integer value) {
            addCriterion("`mode` =", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeNotEqualTo(Integer value) {
            addCriterion("`mode` <>", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeGreaterThan(Integer value) {
            addCriterion("`mode` >", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeGreaterThanOrEqualTo(Integer value) {
            addCriterion("`mode` >=", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeLessThan(Integer value) {
            addCriterion("`mode` <", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeLessThanOrEqualTo(Integer value) {
            addCriterion("`mode` <=", value, "mode");
            return (Criteria) this;
        }

        public Criteria andModeIn(List<Integer> values) {
            addCriterion("`mode` in", values, "mode");
            return (Criteria) this;
        }

        public Criteria andModeNotIn(List<Integer> values) {
            addCriterion("`mode` not in", values, "mode");
            return (Criteria) this;
        }

        public Criteria andModeBetween(Integer value1, Integer value2) {
            addCriterion("`mode` between", value1, value2, "mode");
            return (Criteria) this;
        }

        public Criteria andModeNotBetween(Integer value1, Integer value2) {
            addCriterion("`mode` not between", value1, value2, "mode");
            return (Criteria) this;
        }

        public Criteria andModemsgIsNull() {
            addCriterion("modeMsg is null");
            return (Criteria) this;
        }

        public Criteria andModemsgIsNotNull() {
            addCriterion("modeMsg is not null");
            return (Criteria) this;
        }

        public Criteria andModemsgEqualTo(String value) {
            addCriterion("modeMsg =", value, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgNotEqualTo(String value) {
            addCriterion("modeMsg <>", value, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgGreaterThan(String value) {
            addCriterion("modeMsg >", value, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgGreaterThanOrEqualTo(String value) {
            addCriterion("modeMsg >=", value, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgLessThan(String value) {
            addCriterion("modeMsg <", value, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgLessThanOrEqualTo(String value) {
            addCriterion("modeMsg <=", value, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgLike(String value) {
            addCriterion("modeMsg like", value, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgNotLike(String value) {
            addCriterion("modeMsg not like", value, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgIn(List<String> values) {
            addCriterion("modeMsg in", values, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgNotIn(List<String> values) {
            addCriterion("modeMsg not in", values, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgBetween(String value1, String value2) {
            addCriterion("modeMsg between", value1, value2, "modemsg");
            return (Criteria) this;
        }

        public Criteria andModemsgNotBetween(String value1, String value2) {
            addCriterion("modeMsg not between", value1, value2, "modemsg");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}