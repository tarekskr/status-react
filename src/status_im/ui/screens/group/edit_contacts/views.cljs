(ns status-im.ui.screens.group.edit-contacts.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [re-frame.core :as re-frame]
            [status-im.ui.components.contact.contact :refer [contact-view]]
            [status-im.ui.components.list.views :as list]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.status-bar.view :refer [status-bar]]
            [status-im.ui.components.toolbar.view :as toolbar]
            [status-im.ui.screens.group.styles :as styles]
            [status-im.i18n :as i18n]))

(defn contacts-list [contacts extended? extend-options]
  [react/view {:flex 1}
   [list/flat-list {:data                      contacts
                    :enableEmptySections       true
                    :render-fn                 (fn [contact]
                                                 [contact-view {:contact        contact
                                                                :extended?      extended?
                                                                :extend-options (extend-options contact)}])
                    :bounces                   false
                    :keyboardShouldPersistTaps :always
                    :footer                    list/default-footer
                    :header                    list/default-header}]])

(defn chat-extended-options [item]
  [{:action #(re-frame/dispatch [:remove-group-chat-participants #{(:whisper-identity item)}])
    :label  (i18n/label :t/remove)}])

(defn contact-extended-options [group-id]
  (fn [item]
    [{:action               #(re-frame/dispatch [:remove-contact-from-group
                                                 (:whisper-identity item)
                                                 group-id])
      :accessibility-label :remove-button
      :label               (i18n/label :t/remove-from-group)}]))

(defview edit-chat-group-contact-list []
  (letsubs [chat-name [:chat :name]
            contacts [:current-chat-contacts]
            current-pk [:get :current-public-key]
            group-admin [:chat :group-admin]]
    (let [admin? (= current-pk group-admin)]
      [react/view styles/group-container
       [status-bar]
       [toolbar/simple-toolbar chat-name]
       [contacts-list
        contacts
        admin?
        chat-extended-options]])))

(defview contacts-list-view [group-id]
  (letsubs [contacts [:all-added-group-contacts group-id]]
    [contacts-list
     contacts
     true
     (contact-extended-options group-id)]))

(defview edit-contact-group-contact-list []
  (letsubs [group [:get-contact-group]]
    [react/view styles/group-container
     [status-bar]
     [toolbar/simple-toolbar (:name group)]
     [contacts-list-view (:group-id group)]]))
