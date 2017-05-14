(ns swarmpit.component.network.info
  (:require [swarmpit.uri :refer [dispatch!]]
            [swarmpit.material :as material]
            [swarmpit.component.message :as message]
            [rum.core :as rum]
            [ajax.core :as ajax]))

(enable-console-print!)

(defn- delete-network-handler
  [network-id]
  (ajax/DELETE (str "/networks/" network-id)
               {:handler       (fn [_]
                                 (let [message (str "Network " network-id " has been removed.")]
                                   (dispatch! "/#/networks")
                                   (message/mount! message)))
                :error-handler (fn [{:keys [status status-text]}]
                                 (let [message (str "Network " network-id " removing failed. Reason: " status-text)]
                                   (message/mount! message)))}))

(rum/defc form < rum/static [item]
  [:div
   [:div.form-panel
    [:div.form-panel-right
     (material/theme
       (material/raised-button
         #js {:onTouchTap delete-network-handler
              :label      "Delete"}))]]
   [:div.form-view
    [:div.form-view-group
     (material/form-view-section "General settings")
     (material/form-view-row "ID" (:id item))
     (material/form-view-row "NAME" (:name item))
     (material/form-view-row "CREATED" (:created item))
     (material/form-view-row "DRIVER" (:driver item))
     (material/form-view-row "INTERNAL" (if (:internal item)
                                          "yes"
                                          "no"))
     (material/form-view-section "IP address management")
     (material/form-view-row "SUBNET" (:subnet item))
     (material/form-view-row "GATEWAY" (:gateway item))]]])

(defn mount!
  [item]
  (rum/mount (form item) (.getElementById js/document "content")))