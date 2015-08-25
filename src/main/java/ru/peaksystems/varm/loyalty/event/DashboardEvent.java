package ru.peaksystems.varm.loyalty.event;

import lombok.Getter;
import ru.peak.ml.loyalty.core.data.CardOperation;
import ru.peak.ml.loyalty.core.data.Holder;
import ru.peak.ml.loyalty.core.data.MlUser;
import ru.peaksystems.varm.loyalty.domain.Transaction;
import ru.peaksystems.varm.loyalty.domain.dto.CardOperationFilterParameters;
import ru.peaksystems.varm.loyalty.view.DashboardViewType;

import java.util.Collection;

public abstract class DashboardEvent {

    public static final class UserLoginRequestedEvent {
        private final MlUser user;

        public MlUser getUser(){
            return user;
        }

        public UserLoginRequestedEvent(final MlUser user) {
            this.user = user;
        }
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

    public static final class TransactionReportEvent {
        private final Collection<Transaction> transactions;

        public TransactionReportEvent(final Collection<Transaction> transactions) {
            this.transactions = transactions;
        }

        public Collection<Transaction> getTransactions() {
            return transactions;
        }
    }

    public static final class PostViewChangeEvent {
        private final DashboardViewType view;

        public PostViewChangeEvent(final DashboardViewType view) {
            this.view = view;
        }

        public DashboardViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
    }

    /**
     * Событие - найден держатель
     */
    public static final class CardholderFindEvent {
        private final Holder holder;

        public CardholderFindEvent(final Holder holder) {
            this.holder = holder;
        }

        public Holder getHolder() {
            return holder;
        }
    }

    public static final class CardholderClearEvent {
    }

    public static final class CardholderUpdateEvent {
        private final Holder holder;

        public CardholderUpdateEvent(final Holder holder) {
            this.holder = holder;
        }

        public Holder getHolder() {
            return holder;
        }
    }

    /**
     * Изменился набор операций
     */
    public static final class ChangeCardOperationsEvent {

    }

    public static final class ClickCardOperationEvent {
        CardOperation cardOperation;

        public CardOperation getCardOperation(){
            return cardOperation;
        }

        public ClickCardOperationEvent(CardOperation cardOperation){
            this.cardOperation = cardOperation;
        }
    }

    public static final class CardOperaionFilterEvent {
        private final CardOperationFilterParameters cardOperationFilterParameters;

        public CardOperaionFilterEvent(final CardOperationFilterParameters cardOperationFilterParameters) {
            this.cardOperationFilterParameters = cardOperationFilterParameters;
        }

        public CardOperationFilterParameters getCardOperationFilterParameters() {
            return cardOperationFilterParameters;        }
    }
}
