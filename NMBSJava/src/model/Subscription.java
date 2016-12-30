package model;

import java.time.Instant;
import java.util.UUID;

public class Subscription {
	private UUID subscriptionID;
	private UUID railCardID;
	private UUID routeID;
	private UUID discountID;
	private String validFrom;
	private String validUntil;
	private RailCard railCard;
	private Route route;
	private Discount discount;
	private long lastUpdated;

	public Subscription() {
	}

	public Subscription(UUID railID, UUID routeID, UUID discountID, String validFrom, String validUntil) {
		this.railCardID = railID;
		this.routeID = routeID;
		this.discountID = discountID;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.subscriptionID = UUID.randomUUID();
		this.lastUpdated = Instant.now().getEpochSecond();
	}

	public UUID getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(UUID subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public UUID getRailCardID() {
		return railCardID;
	}

	public void setRailCardID(UUID railCardID) {
		this.railCardID = railCardID;
	}

	public UUID getRouteID() {
		return routeID;
	}

	public void setRouteID(UUID routeID) {
		this.routeID = routeID;
	}

	public UUID getDiscountID() {
		return discountID;
	}

	public void setDiscountID(UUID discountID) {
		this.discountID = discountID;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}

	public RailCard getRailCard() {
		return railCard;
	}

	public void setRailCard(RailCard railCard) {
		this.railCard = railCard;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void update() {
		this.lastUpdated = Instant.now().getEpochSecond();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((discountID == null) ? 0 : discountID.hashCode());
		result = prime * result + ((railCardID == null) ? 0 : railCardID.hashCode());
		result = prime * result + ((routeID == null) ? 0 : routeID.hashCode());
		result = prime * result + ((subscriptionID == null) ? 0 : subscriptionID.hashCode());
		result = prime * result + ((validFrom == null) ? 0 : validFrom.hashCode());
		result = prime * result + ((validUntil == null) ? 0 : validUntil.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscription other = (Subscription) obj;
		if (discountID == null) {
			if (other.discountID != null)
				return false;
		} else if (!discountID.equals(other.discountID))
			return false;
		if (railCardID == null) {
			if (other.railCardID != null)
				return false;
		} else if (!railCardID.equals(other.railCardID))
			return false;
		if (routeID == null) {
			if (other.routeID != null)
				return false;
		} else if (!routeID.equals(other.routeID))
			return false;
		if (subscriptionID == null) {
			if (other.subscriptionID != null)
				return false;
		} else if (!subscriptionID.equals(other.subscriptionID))
			return false;
		if (validFrom == null) {
			if (other.validFrom != null)
				return false;
		} else if (!validFrom.equals(other.validFrom))
			return false;
		if (validUntil == null) {
			if (other.validUntil != null)
				return false;
		} else if (!validUntil.equals(other.validUntil))
			return false;
		return true;
	}

}