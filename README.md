# test
zz

```
public Mono<List<Audit>> searchByKey(String accountNumber, String accountName, String dateDebut, String dateFin)  {
        StringBuilder queryBulder = new StringBuilder();
        MutableBoolean hasWhere = new MutableBoolean();
        Map<String, Object> parameterMap = new HashMap<>();
        queryBulder.append("SELECT a from Audit a ");
        if(StringUtils.isNotEmpty(accountNumber)){
            queryBulder.append(hasWhere.isTrue() ? "AND " : "WHERE ").append("a.identification like :identification ");
            hasWhere.setValue(true);
            parameterMap.put("identification", accountNumber);
        }
        if(StringUtils.isNotEmpty(accountName)){
            queryBulder.append(hasWhere.isTrue() ? "AND " : "WHERE ").append("a.inputAccountName like :inputAccountName ");
            hasWhere.setValue(true);
            parameterMap.put("inputAccountName", accountName);
        }
        if(StringUtils.isNotEmpty(dateDebut) && StringUtils.isNotEmpty(dateFin)){
            try {
                Timestamp  dateDebutTimeStamps =   this.getDateTimeStamps(dateDebut);
                Timestamp  dateFinTimeStamps =   this.getDateTimeStamps(dateFin);
                queryBulder.append(hasWhere.isTrue() ? "AND " : "WHERE ").append("a.dateAudit BETWEEN :dateAuditLow AND :dateAuditHigh ");
                hasWhere.setValue(true);
                parameterMap.put("dateAuditLow", dateDebutTimeStamps);
                parameterMap.put("dateAuditHigh", dateFinTimeStamps);
            }catch (ParseException e){
                log.error("fail to parse date");
            }
        }

        TypedQuery<Audit>  query = this.entityManager.createQuery(queryBulder.toString(), Audit.class);
        parameterMap.forEach(query::setParameter);
        List<Audit> audits = query.getResultList();
        return Mono.just(audits);
    }
```

or

```    public Mono<List<Audit>> searchByKey(String accountNumber, String accountName, String dateDebut, String dateFin) throws ParseException {
       CriteriaBuilder cb = entityManager.getCriteriaBuilder();
       CriteriaQuery cq= cb.createQuery();
        Root<Audit> audit = cq.from(Audit.class);
        //Constructing list of parameters
        List<Predicate> predicates = new ArrayList<>();

        if(StringUtils.isNotEmpty(accountNumber)){
            predicates.add(cb.like(audit.get("identification"), accountNumber));
        }
        if(StringUtils.isNotEmpty(accountName)){
            predicates.add(cb.like(audit.get("inputAccountName"), accountName));
        }
        if(StringUtils.isNotEmpty(dateDebut) && StringUtils.isNotEmpty(dateFin)){
            predicates.add(cb.and(cb.between(audit.get("dateAudit"), getDateTimeStamps(dateDebut), getDateTimeStamps(dateDebut))));
        }

        cq.select(audit).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
       List<Audit>  audits = entityManager.createQuery(cq).getResultList();
        return Mono.just(audits);
    }

    public Timestamp getDateTimeStamps(String date) throws ParseException {
        return  new Timestamp(DateUtils.parseDate(date,"yyyy-MM-dd").getTime());
    }
```
