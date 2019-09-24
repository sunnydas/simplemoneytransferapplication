package com.transfer.money.test.functional;

import com.transfer.money.app.RESTRouter;
import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.ExceptionHandler;
import com.transfer.money.repository.domain.Account;
import com.transfer.money.repository.domain.AccountUser;
import com.transfer.money.repository.domain.MoneyTransferTx;
import com.transfer.money.service.domain.AccountDomainObject;
import com.transfer.money.service.domain.Transfer;
import com.transfer.money.service.domain.UserDomainObject;
import com.transfer.money.test.config.ConfigManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static com.transfer.money.test.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

public class APIFunctionalTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Spark.port(Integer.parseInt(getPort()));
        RESTRouter.initRouters();
        awaitInitialization();

    }

    @AfterClass
    public static void tearDown() throws Exception {
        stop();
    }

    @Test
    public void testCreateUser() {
        Response response = createUser("bclara");
        assertThat(response.getStatus()).isEqualTo(201);
        UserDomainObject userDomainObject1 = response.readEntity(AccountUser.class);
        System.out.println(userDomainObject1);
        assertThat(userDomainObject1).isNotNull();
        assertThat(userDomainObject1.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserInvalidUserName() {
        Response response = createUser(null);
        assertThat(response.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void testCreateUserInvalidUserNameLongString() {
        Response response = createUser("adkasjdajsajlkdasjlksajlksajdsajdsajdlsajdlksajdlksajdlksajdlksajdlksa" +
                "adka;lsdksakd;lsajdlksajdlksajdlksajdlksajdlksajdlksajdsal" +
                "asldsaldsadsadajdlksajlasahfahfuwfyuewfhhreryquijyf9hdxkpeqdxwf98hedxof9refhrxfjowjfojjijicjoicjwijci" +
                "sdjlksadjlksajddlksajdlksajdlksajdksajdlkasjdlajdieuwu230ur32029i-39e23e20-iddikdakd;lakd;lalkd;ldk;daadkasjdajsajlkdasjlksajlksajdsajdsajdlsajdlksajdlksajdlksajdlksajdlksa\" +\n" +
                "                \"adka;lsdksakd;lsajdlksajdlksajdlksajdlksajdlksajdlksajdsal\" +\n" +
                "                \"asldsaldsadsadajdlksajlasahfahfuwfyuewfhhreryquijyf9hdxkpeqdxwf98hedxof9refhrxfjowjfojjijicjoicjwijci\" +\n" +
                "                \"sdjlksadjlksajddlksajdlksajdlksajdksajdlkasjdlajdieuwu230ur32029i-39e23e20-iddikdakd;lakd;lalkd;ldk;da" +
                "adkasjdajsajlkdasjlksajlksajdsajdsajdlsajdlksajdlksajdlksajdlksajdlksa\" +\n" +
                "                \"adka;lsdksakd;lsajdlksajdlksajdlksajdlksajdlksajdlksajdsal\" +\n" +
                "                \"asldsaldsadsadajdlksajlasahfahfuwfyuewfhhreryquijyf9hdxkpeqdxwf98hedxof9refhrxfjowjfojjijicjoicjwijci\" +\n" +
                "                \"sdjlksadjlksajddlksajdlksajdlksajdksajdlkasjdlajdieuwu230ur32029i-39e23e20-iddikdakd;lakd;lalkd;ldk;da" +
                "adkasjdajsajlkdasjlksajlksajdsajdsajdlsajdlksajdlksajdlksajdlksajdlksa\" +\n" +
                "                \"adka;lsdksakd;lsajdlksajdlksajdlksajdlksajdlksajdlksajdsal\" +\n" +
                "                \"asldsaldsadsadajdlksajlasahfahfuwfyuewfhhreryquijyf9hdxkpeqdxwf98hedxof9refhrxfjowjfojjijicjoicjwijci\" +\n" +
                "                \"sdjlksadjlksajddlksajdlksajdlksajdksajdlkasjdlajdieuwu230ur32029i-39e23e20-iddikdakd;lakd;lalkd;ldk;da");
        assertThat(response.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void testCreateUserDuplicateUserName() {
        Response response = createUser("turing");
        assertThat(response.getStatus()).isEqualTo(201);
        UserDomainObject userDomainObject1 = response.readEntity(AccountUser.class);
        assertThat(userDomainObject1).isNotNull();
        assertThat(userDomainObject1.getId()).isGreaterThan(0);
        response = createUser("turing");
        assertThat(response.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void testCreateUserUserDefinedId() {
        Response response = createUser("gregory",200);
        assertThat(response.getStatus()).isEqualTo(201);
        UserDomainObject userDomainObject1 = response.readEntity(AccountUser.class);
        assertThat(userDomainObject1).isNotNull();
        assertThat(userDomainObject1.getId()).isGreaterThan(0);
        assertThat(userDomainObject1.getId()).isNotEqualTo(200);
    }


    private Response createAccount(String accountName,String userName) {
        Response response = createUser(userName);
        UserDomainObject userDomainObject = response.readEntity(AccountUser.class);
        long userId = userDomainObject.getId();
        Client client = ClientBuilder.newBuilder().build();
        AccountDomainObject accountDomainObject = populateAccount();
        accountDomainObject.setUserId(userId);
        accountDomainObject.setAccountName(accountName);
        return client.target(URI.create(getResourcePathForAccount()))
                .request()
                .post(Entity.entity(accountDomainObject, MediaType.APPLICATION_JSON));
    }

    private Response createAccountInvalidUser(String accountName) {
        long userId = Long.MAX_VALUE;
        Client client = ClientBuilder.newBuilder().build();
        AccountDomainObject accountDomainObject = populateAccount();
        accountDomainObject.setUserId(userId);
        accountDomainObject.setAccountName(accountName);
        return client.target(URI.create(getResourcePathForAccount()))
                .request()
                .post(Entity.entity(accountDomainObject, MediaType.APPLICATION_JSON));
    }

    private Response createAccountAlt(String accountName,String userName,String currencyCode) {
        Response response = createUser(userName);
        UserDomainObject userDomainObject = response.readEntity(AccountUser.class);
        long userId = userDomainObject.getId();
        Client client = ClientBuilder.newBuilder().build();
        AccountDomainObject accountDomainObject = populateAccount();
        accountDomainObject.setUserId(userId);
        accountDomainObject.setAccountName(accountName);
        accountDomainObject.setCurrencyCode(currencyCode);
        return client.target(URI.create(getResourcePathForAccount()))
                .request()
                .post(Entity.entity(accountDomainObject, MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetUser() {
        Response response = createUser("srtendulkar");
        assertThat(response.getStatus()).isEqualTo(201);
        UserDomainObject userDomainObject = response.readEntity(AccountUser.class);
        assertThat(userDomainObject).isNotNull();
        long userId = userDomainObject.getId();
        assertThat(userId).isGreaterThan(0);
        response = getUser(userId);
        assertThat(response.getStatus()).isEqualTo(200);
        UserDomainObject userDomainObject1 = response.readEntity(AccountUser.class);
        assertThat(userDomainObject).isEqualTo(userDomainObject1);
    }

    @Test
    public void testGetUserNonExistent() {
        long userId = Long.MAX_VALUE;
        Response response = getUser(userId);
        assertThat(response.getStatus()).isEqualTo(404);
        ExceptionHandler exceptionHandler = response.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.NOT_FOUND);
    }


    private Response getUser(long id) {
        Client client = ClientBuilder.newBuilder().build();
        return client.target(URI.create(getResourcePathForAccountUsersFetch(id)))
                .request()
                .get();
    }

    @Test
    public void testCreateAccount() {
        Response response = createAccount("sanga-acc","sangakara");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateAccountInavlidAccName() {
        Response response = createAccount("kdlakdka;dka;dka;lskdlsakd;lsakd;lsakd;lsakd;lsakd;lsadk;lsakd;" +
                "lsaka;lsdk;lsakd;laskd;laskd;lsakd;lsakd;lsakd;lsakd;lsadkkdlakdka;dka;dka;lskdlsakd;lsakd;lsakd;lsakd;lsakd;lsadk;lsakd;\" +\n" +
                "                \"lsaka;lsdk;lsakd;laskd;laskd;lsakd;lsakd;lsakd;lsakd;lsadk" +
                "kdlakdka;dka;dka;lskdlsakd;lsakd;lsakd;lsakd;lsakd;lsadk;lsakd;\" +\n" +
                "                \"lsaka;lsdk;lsakd;laskd;laskd;lsakd;lsakd;lsakd;lsakd;lsadk" +
                "kdlakdka;dka;dka;lskdlsakd;lsakd;lsakd;lsakd;lsakd;lsadk;lsakd;\" +\n" +
                "                \"lsaka;lsdk;lsakd;laskd;laskd;lsakd;lsakd;lsakd;lsakd;lsadk" +
                "dadaskdlakdka;dka;dka;lskdlsakd;lsakd;lsakd;lsakd;lsakd;lsadk;lsakd;\" +\n" +
                "                \"lsaka;lsdk;lsakd;laskd;laskd;lsakd;lsakd;lsakd;lsakd;lsadkkdlakdka;dka;dka;lskdlsakd;lsakd;lsakd;lsakd;lsakd;lsadk;lsakd;\\\" +\\n\" +\n" +
                "                \"                \\\"lsaka;lsdk;lsakd;laskd;laskd;lsakd;lsakd;lsakd;lsakd;lsadk\" +\n" +
                "                \"kdlakdka;dka;dka;lskdlsakd;lsakd;lsakd;lsakd;lsakd;lsadk;lsakd;\\\" +\\n\" +\n" +
                "                \"                \\\"lsaka;lsdk;lsakd;laskd;laskd;lsakd;lsakd;lsakd;lsakd;lsadk\" +\n" +
                "                \"kdlakdka;dka;dka;lskdlsakd;lsakd;lsakd;lsakd;lsakd;lsadk;lsakd;\\\" +\\n\" +\n" +
                "                \"                \\\"lsaka;lsdk;lsakd;laskd;laskd;lsakd;lsakd;lsakd;lsakd;lsadk\" +\n" +
                "                \"dadas","dummy");
        assertThat(response.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void testCreateAccountInvalidAccountName() {
        Response response = createAccount(null,"sangakara");
        assertThat(response.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void testCreateAccountInvalidUser() {
        Response response = createAccountInvalidUser("voldemort");
        assertThat(response.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void testCreateAccountDuplicate() {
        Response response = createAccount("jose-acc","covalco");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        response = createAccount("jose-acc","messi");
        assertThat(response.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void testCreateMultipleAccountsForSameUser() {
        Response response = createUser("richierich");
        UserDomainObject userDomainObject = response.readEntity(AccountUser.class);
        long userId = userDomainObject.getId();
        response = createAccount("mrbuffet-acc",userId);
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        response = createAccount("warren-acc",userId);
        assertThat(response.getStatus()).isEqualTo(201);
        accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
    }

    private Response createAccount(String accountName,long userId) {
        Client client = ClientBuilder.newBuilder().build();
        AccountDomainObject accountDomainObject = populateAccount();
        accountDomainObject.setUserId(userId);
        accountDomainObject.setAccountName(accountName);
        return client.target(URI.create(getResourcePathForAccount()))
                .request()
                .post(Entity.entity(accountDomainObject, MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAccount() {
        Response response = createAccount("sach-acc1","sach");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        long id = accountDomainObject.getId();
        assertThat(id).isGreaterThan(0);
        Response response1 = getAccount(id);
        assertThat(response1.getStatus()).isEqualTo(200);
        AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
        assertThat(accountDomainObject).isEqualTo(accountDomainObject1);
    }

    @Test
    public void testGetAccountNonExistent() {
        long id = Long.MAX_VALUE;
        Response response = getAccount(id);
        assertThat(response.getStatus()).isEqualTo(404);
        ExceptionHandler exceptionHandler = response.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.NOT_FOUND);
    }

    private Response getAccount(long id){
        Client client = ClientBuilder.newBuilder().build();
        return client.target(URI.create(getResourcePathForAccountFetch(id)))
                .request()
                .get();
    }

    private Response createUser(String userName) {
        Client client = ClientBuilder.newBuilder().build();
        UserDomainObject userDomainObject = populateUser();
        userDomainObject.setUserName(userName);
        return client.target(URI.create(getResourcePathForAccountUsers()))
                .request()
                .post(Entity.entity(userDomainObject, MediaType.APPLICATION_JSON));
    }

    private Response createUser(String userName,long id) {
        Client client = ClientBuilder.newBuilder().build();
        UserDomainObject userDomainObject = populateUser();
        userDomainObject.setUserName(userName);
        userDomainObject.setId(id);
        return client.target(URI.create(getResourcePathForAccountUsers()))
                .request()
                .post(Entity.entity(userDomainObject, MediaType.APPLICATION_JSON));
    }



    @Test
    public void testUpdateAccount() {
        Response response = createAccount("lasith-acc","malinga");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        accountDomainObject.setBalance(new BigDecimal("400"));
        Response response1 = updateAccount(accountDomainObject);
        assertThat(response1.getStatus()).isEqualTo(200);
        AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
        assertThat(accountDomainObject1).isNotNull();
        assertThat(accountDomainObject1.getId()).isEqualTo(accountDomainObject.getId());
        assertThat(accountDomainObject1.getBalance().toString()).isEqualTo("400");
    }

    @Test
    public void testUpdateAccountNonExistent() {
        AccountDomainObject accountDomainObject = new Account();
        Response response = updateAccount(accountDomainObject);
        assertThat(response.getStatus()).isEqualTo(404);
        ExceptionHandler exceptionHandler = response.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.NOT_FOUND);
    }

    @Test
    public void transferBetweenAccounts(){
        Response response = createAccount("sourav-acc","ganguly");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        Response response1 = createAccount("kane-acc","kanewilliamson");
        assertThat(response1.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
        assertThat(accountDomainObject1).isNotNull();
        assertThat(accountDomainObject1.getId()).isGreaterThan(0);
        assertThat(accountDomainObject1.getBalance().toString()).isEqualTo("100");
        Response response2 = transfer(accountDomainObject,accountDomainObject1,new BigDecimal("10"));
        assertThat(response2.getStatus()).isEqualTo(202);
        Transfer transfer = response2.readEntity(MoneyTransferTx.class);
        assertThat(transfer.getStatus()).isEqualTo("SUCCESS");
        Response srcResponse = getAccount(accountDomainObject.getId());
        AccountDomainObject src = srcResponse.readEntity(Account.class);
        assertThat(src.getBalance().toString()).isEqualTo("90");
        Response tgtResponse = getAccount(accountDomainObject1.getId());
        AccountDomainObject tgt = tgtResponse.readEntity(Account.class);
        assertThat(tgt.getBalance()).isEqualTo("110");
    }

    private Response transfer(AccountDomainObject source,AccountDomainObject target,BigDecimal transferAmount){
        Transfer transfer = new MoneyTransferTx();
        transfer.setSourceAccountId(source.getId());
        transfer.setTargetAccountId(target.getId());
        transfer.setTransferAmount(transferAmount);
        transfer.setDateOfTransfer(new Date(System.currentTimeMillis()));
        Client client = ClientBuilder.newBuilder().build();
        return client.target(URI.create(getResourcePathForMoneyTransfer()))
                .request()
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON));
    }

    @Test
    public void transferBetweenAccountsInsufficientFundsAtSource(){
        Response response = createAccount("gambhir-acc","gambhir");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        Response response1 = createAccount("kohli-acc","kohli");
        assertThat(response1.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
        assertThat(accountDomainObject1).isNotNull();
        assertThat(accountDomainObject1.getId()).isGreaterThan(0);
        assertThat(accountDomainObject1.getBalance().toString()).isEqualTo("100");
        Response response2 = transfer(accountDomainObject,accountDomainObject1,
                new BigDecimal("200"));
        assertThat(response2.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response2.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void transferBetweenAccountsIncompatibleCurrencyCode(){
        Response response = createAccountAlt("steve-acc","stevesmith","USD");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        Response response1 = createAccount("warner-acc","warner");
        assertThat(response1.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
        assertThat(accountDomainObject1).isNotNull();
        assertThat(accountDomainObject1.getId()).isGreaterThan(0);
        assertThat(accountDomainObject1.getBalance().toString()).isEqualTo("100");
        Response response2 = transfer(accountDomainObject,accountDomainObject1,
                new BigDecimal("20"));
        assertThat(response2.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response2.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void transferBetweenAccountsInvalidCurrencyCode(){
        Response response = createAccountAlt("braithwhite-acc","carlos","XKC");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        Response response1 = createAccount("sunil-acc","narine");
        assertThat(response1.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
        assertThat(accountDomainObject1).isNotNull();
        assertThat(accountDomainObject1.getId()).isGreaterThan(0);
        assertThat(accountDomainObject1.getBalance().toString()).isEqualTo("100");
        Response response2 = transfer(accountDomainObject,accountDomainObject1,
                new BigDecimal("20"));
        assertThat(response2.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response2.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void transferBetweenAccountsNonExixtentAccount(){
        Response response = createAccountAlt("joe-acc","joeroot","USD");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        Response response2 = transfer(accountDomainObject,new Account(),
                new BigDecimal("20"));
        assertThat(response2.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response2.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void transferBetweenSameAccount(){
        Response response = createAccountAlt("noel-acc","noel","USD");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        Response response2 = transfer(accountDomainObject,accountDomainObject,
                new BigDecimal("20"));
        assertThat(response2.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response2.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void transferBetweenAccountsNegativeTransfer(){
        Response response = createAccount("ben-acc","benstokes");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        Response response1 = createAccount("alistair-acc","cook");
        assertThat(response1.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
        assertThat(accountDomainObject1).isNotNull();
        assertThat(accountDomainObject1.getId()).isGreaterThan(0);
        assertThat(accountDomainObject1.getBalance().toString()).isEqualTo("100");
        Response response2 = transfer(accountDomainObject,accountDomainObject1,new BigDecimal("-10"));
        assertThat(response2.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response2.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
    }

    @Test
    public void transferBetweenAccountsMultipleTx(){
        Response response = createAccount("colin-acc","colin");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        Response response1 = createAccount("wayne-acc","wayne");
        assertThat(response1.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
        assertThat(accountDomainObject1).isNotNull();
        assertThat(accountDomainObject1.getId()).isGreaterThan(0);
        assertThat(accountDomainObject1.getBalance().toString()).isEqualTo("100");
        Response response2 = transfer(accountDomainObject,accountDomainObject1,new BigDecimal("10"));
        assertThat(response2.getStatus()).isEqualTo(202);
        Transfer transfer = response2.readEntity(MoneyTransferTx.class);
        assertThat(transfer.getStatus()).isEqualTo("SUCCESS");
        Response srcResponse = getAccount(accountDomainObject.getId());
        AccountDomainObject src = srcResponse.readEntity(Account.class);
        assertThat(src.getBalance().toString()).isEqualTo("90");
        Response tgtResponse = getAccount(accountDomainObject1.getId());
        AccountDomainObject tgt = tgtResponse.readEntity(Account.class);
        assertThat(tgt.getBalance()).isEqualTo("110");
        response2 = transfer(accountDomainObject,accountDomainObject1,new BigDecimal("50"));
        assertThat(response2.getStatus()).isEqualTo(202);
        transfer = response2.readEntity(MoneyTransferTx.class);
        assertThat(transfer.getStatus()).isEqualTo("SUCCESS");
        srcResponse = getAccount(accountDomainObject.getId());
        src = srcResponse.readEntity(Account.class);
        assertThat(src.getBalance().toString()).isEqualTo("40");
        tgtResponse = getAccount(accountDomainObject1.getId());
        tgt = tgtResponse.readEntity(Account.class);
        assertThat(tgt.getBalance()).isEqualTo("160");
        response2 = transfer(accountDomainObject,accountDomainObject1,new BigDecimal("40"));
        assertThat(response2.getStatus()).isEqualTo(202);
        transfer = response2.readEntity(MoneyTransferTx.class);
        assertThat(transfer.getStatus()).isEqualTo("SUCCESS");
        srcResponse = getAccount(accountDomainObject.getId());
        src = srcResponse.readEntity(Account.class);
        assertThat(src.getBalance().toString()).isEqualTo("0");
        tgtResponse = getAccount(accountDomainObject1.getId());
        tgt = tgtResponse.readEntity(Account.class);
        assertThat(tgt.getBalance()).isEqualTo("200");
        response2 = transfer(accountDomainObject,accountDomainObject1,new BigDecimal("40"));
        assertThat(response2.getStatus()).isEqualTo(400);
        ExceptionHandler exceptionHandler = response2.readEntity(ExceptionHandler.class);
        assertThat(exceptionHandler.getExceptionCode()).isEqualByComparingTo(ExceptionCode.VALIDATION_ERROR);
        response2 = transfer(accountDomainObject1,accountDomainObject,new BigDecimal("90"));
        assertThat(response2.getStatus()).isEqualTo(202);
        transfer = response2.readEntity(MoneyTransferTx.class);
        assertThat(transfer.getStatus()).isEqualTo("SUCCESS");
        srcResponse = getAccount(accountDomainObject.getId());
        src = srcResponse.readEntity(Account.class);
        assertThat(src.getBalance().toString()).isEqualTo("90");
        tgtResponse = getAccount(accountDomainObject1.getId());
        tgt = tgtResponse.readEntity(Account.class);
        assertThat(tgt.getBalance()).isEqualTo("110");
        response2 = transfer(accountDomainObject1,accountDomainObject,new BigDecimal("10"));
        assertThat(response2.getStatus()).isEqualTo(202);
        transfer = response2.readEntity(MoneyTransferTx.class);
        assertThat(transfer.getStatus()).isEqualTo("SUCCESS");
        srcResponse = getAccount(accountDomainObject.getId());
        src = srcResponse.readEntity(Account.class);
        assertThat(src.getBalance().toString()).isEqualTo("100");
        tgtResponse = getAccount(accountDomainObject1.getId());
        tgt = tgtResponse.readEntity(Account.class);
        assertThat(tgt.getBalance()).isEqualTo("100");
    }

    @Test
    public void transferBetweenAccountsMultipleThreads() throws InterruptedException {
        Response response = createAccount("harris-acc","harrison");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        Response response1 = createAccount("kole-acc","kole");
        assertThat(response1.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
        assertThat(accountDomainObject1).isNotNull();
        assertThat(accountDomainObject1.getId()).isGreaterThan(0);
        assertThat(accountDomainObject1.getBalance().toString()).isEqualTo("100");
        CountDownLatch countDownLatch = new CountDownLatch(4);
        Runnable transferTask1 = () -> { Response response2 = transfer(accountDomainObject,accountDomainObject1,new BigDecimal("10"));
                                    countDownLatch.countDown();
            assertThat(response2.getStatus()).isEqualTo(201); };
        Runnable transferTask2 = () -> {Response response2 = transfer(accountDomainObject,accountDomainObject1,new BigDecimal("10"));
        countDownLatch.countDown();
        assertThat(response2.getStatus()).isEqualTo(201);};
        Runnable transferTask3 = () -> { Response response2 = transfer(accountDomainObject,accountDomainObject1,new BigDecimal("10"));
            countDownLatch.countDown();
            assertThat(response2.getStatus()).isEqualTo(201); };
        Runnable transferTask4 = () -> {Response response2 = transfer(accountDomainObject,accountDomainObject1,new BigDecimal("10"));
            countDownLatch.countDown();
        assertThat(response2.getStatus()).isEqualTo(201);};
        Thread thread1 = new Thread(transferTask1);
        Thread thread2 = new Thread(transferTask2);
        Thread thread3 = new Thread(transferTask3);
        Thread thread4 = new Thread(transferTask4);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        countDownLatch.await();
        Assert.assertTrue(true);
    }

    @Test
    public void testCreateUserMultipleThreads() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(4);
        Runnable task1 = () -> { Response response = createUser("kumail");
            assertThat(response.getStatus()).isEqualTo(201);
            UserDomainObject userDomainObject1 = response.readEntity(AccountUser.class);
            assertThat(userDomainObject1).isNotNull();
            assertThat(userDomainObject1.getId()).isGreaterThan(0);
        countDownLatch.countDown();};
        Runnable task2 = () -> {Response response = createUser("jerry");
            assertThat(response.getStatus()).isEqualTo(201);
            UserDomainObject userDomainObject1 = response.readEntity(AccountUser.class);
            assertThat(userDomainObject1).isNotNull();
            assertThat(userDomainObject1.getId()).isGreaterThan(0);
            countDownLatch.countDown();};
        Runnable task3 = () -> { Response response = createUser("trevor");
            assertThat(response.getStatus()).isEqualTo(201);
            UserDomainObject userDomainObject1 = response.readEntity(AccountUser.class);
            assertThat(userDomainObject1).isNotNull();
            assertThat(userDomainObject1.getId()).isGreaterThan(0);
            countDownLatch.countDown();};
        Runnable task4 = () -> { Response response = createUser("milton");
            assertThat(response.getStatus()).isEqualTo(201);
            UserDomainObject userDomainObject1 = response.readEntity(AccountUser.class);
            assertThat(userDomainObject1).isNotNull();
            assertThat(userDomainObject1.getId()).isGreaterThan(0);
            countDownLatch.countDown();};
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread thread3 = new Thread(task3);
        Thread thread4 = new Thread(task4);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        countDownLatch.await();
        Assert.assertTrue(true);
    }

    @Test
    public void testGetUserMultipleThreads() throws InterruptedException {
        Response response = createUser("sheldon");
        assertThat(response.getStatus()).isEqualTo(201);
        UserDomainObject userDomainObject = response.readEntity(AccountUser.class);
        assertThat(userDomainObject).isNotNull();
        long userId1 = userDomainObject.getId();
        assertThat(userId1).isGreaterThan(0);
        response = createUser("moss");
        assertThat(response.getStatus()).isEqualTo(201);
        userDomainObject = response.readEntity(AccountUser.class);
        assertThat(userDomainObject).isNotNull();
        long userId2 = userDomainObject.getId();
        assertThat(userId2).isGreaterThan(0);
        response = createUser("isa");
        assertThat(response.getStatus()).isEqualTo(201);
        userDomainObject = response.readEntity(AccountUser.class);
        assertThat(userDomainObject).isNotNull();
        long userId3 = userDomainObject.getId();
        assertThat(userId3).isGreaterThan(0);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Runnable task1 = () -> { Response response1 = getUser(userId1);
            assertThat(response1.getStatus()).isEqualTo(200);
            UserDomainObject userDomainObject1 = response1.readEntity(AccountUser.class);
            assertThat(userDomainObject1.getId()).isEqualTo(userId1);
            countDownLatch.countDown();};
        Runnable task2 = () -> {Response response1 = getUser(userId2);
            assertThat(response1.getStatus()).isEqualTo(200);
            UserDomainObject userDomainObject1 = response1.readEntity(AccountUser.class);
            assertThat(userDomainObject1.getId()).isEqualTo(userId2);
            countDownLatch.countDown();};
        Runnable task3 = () -> { Response response1 = getUser(userId3);
            assertThat(response1.getStatus()).isEqualTo(200);
            UserDomainObject userDomainObject1 = response1.readEntity(AccountUser.class);
            assertThat(userDomainObject1.getId()).isEqualTo(userId3);
            countDownLatch.countDown();};
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread thread3 = new Thread(task3);
        thread1.start();
        thread2.start();
        thread3.start();
        countDownLatch.await();
        Assert.assertTrue(true);
    }

    @Test
    public void testCreateAccountMultiplThreads() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Runnable task1 = () -> {
            Response response = createAccount("obelisk-acc","obelisk");
            assertThat(response.getStatus()).isEqualTo(201);
            AccountDomainObject accountDomainObject = response.readEntity(Account.class);
            assertThat(accountDomainObject).isNotNull();
            assertThat(accountDomainObject.getId()).isGreaterThan(0);
            countDownLatch.countDown();
        };
        Runnable task2 = () -> {
            Response response = createAccount("calculus-acc","calculus");
            assertThat(response.getStatus()).isEqualTo(201);
            AccountDomainObject accountDomainObject = response.readEntity(Account.class);
            assertThat(accountDomainObject).isNotNull();
            assertThat(accountDomainObject.getId()).isGreaterThan(0);
            countDownLatch.countDown();
        };
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();
        countDownLatch.await();
        Assert.assertTrue(true);
    }

    @Test
    public void testGetAccountMultipleThreads() throws InterruptedException {
        Response response = createAccount("dilbert-acc1","dilbert");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        long id = accountDomainObject.getId();
        assertThat(id).isGreaterThan(0);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Runnable task1 = () -> {
            Response response1 = getAccount(id);
            assertThat(response1.getStatus()).isEqualTo(200);
            AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
            assertThat(accountDomainObject).isEqualTo(accountDomainObject1);
            countDownLatch.countDown();
        };
        Runnable task2 = () -> {
            Response response1 = getAccount(id);
            assertThat(response1.getStatus()).isEqualTo(200);
            AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
            assertThat(accountDomainObject).isEqualTo(accountDomainObject1);
            countDownLatch.countDown();
        };
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();
        countDownLatch.await();
        Assert.assertTrue(true);
    }

    @Test
    public void testUpdateAccountMultipleThreads() throws InterruptedException {
        Response response = createAccount("rammstein-acc","rammstein");
        assertThat(response.getStatus()).isEqualTo(201);
        AccountDomainObject accountDomainObject = response.readEntity(Account.class);
        assertThat(accountDomainObject).isNotNull();
        assertThat(accountDomainObject.getId()).isGreaterThan(0);
        assertThat(accountDomainObject.getBalance().toString()).isEqualTo("100");
        accountDomainObject.setBalance(new BigDecimal("400"));
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Runnable task1 = () -> {
            Response response1 = updateAccount(accountDomainObject);
            assertThat(response1.getStatus()).isEqualTo(200);
            AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
            assertThat(accountDomainObject1).isNotNull();
            assertThat(accountDomainObject1.getId()).isEqualTo(accountDomainObject.getId());
            countDownLatch.countDown();
        };
        Runnable task2 = () -> {
            Response response1 = updateAccount(accountDomainObject);
            assertThat(response1.getStatus()).isEqualTo(200);
            AccountDomainObject accountDomainObject1 = response1.readEntity(Account.class);
            assertThat(accountDomainObject1).isNotNull();
            assertThat(accountDomainObject1.getId()).isEqualTo(accountDomainObject.getId());
            countDownLatch.countDown();
        };
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();
        countDownLatch.await();
        Assert.assertTrue(true);
    }

    private Response updateAccount(AccountDomainObject accountDomainObject){
        Client client = ClientBuilder.newBuilder().build();
        return client.target(URI.create(getResourcePathForAccount()))
                .request()
                .put(Entity.entity(accountDomainObject, MediaType.APPLICATION_JSON));
    }

    private String getResourcePathForMoneyTransfer(){
        return PROTOCOL + getHost(HOST_KEY) + ":" + getPort() +
                TRANSFER_PATH;
    }

    private String getResourcePathForAccountFetch(long accountId) {
        return PROTOCOL + getHost(HOST_KEY) + ":" + getPort() +
                ACCOUNT_PATH + "/" + accountId;
    }

    private String getResourcePathForAccount() {
        return PROTOCOL + getHost(HOST_KEY) + ":" + getPort() +
                ACCOUNT_PATH;
    }

    private String getResourcePathForAccountUsers() {
        return PROTOCOL + getHost(HOST_KEY) + ":" + getPort() +
                ACCOUNT_USERS_PATH;
    }

    private String getResourcePathForAccountUsersFetch(long id) {
        return PROTOCOL + getHost(HOST_KEY) + ":" + getPort() +
                ACCOUNT_USERS_PATH + "/" + id;
    }

    private static String getHost(String hostKey) {
        return ConfigManager.getProperty(hostKey);
    }

    private static String getPort() {
        return getHost(PORT_KEY);
    }
}
